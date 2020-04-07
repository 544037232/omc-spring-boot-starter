package com.pricess.omc.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import java.util.*;

public abstract class AbstractConfiguredObjectBuilder<O, B extends ObjectBuilder<O>>
        extends AbstractObjectBuilder<O> {
    private final Log logger = LogFactory.getLog(getClass());

    private final LinkedHashMap<Class<? extends ObjectConfigurer<O, B>>, List<ObjectConfigurer<O, B>>> configurers = new LinkedHashMap<>();
    private final List<ObjectConfigurer<O, B>> configurersAddedInInitializing = new ArrayList<>();

    private final Map<Class<?>, Object> sharedObjects = new HashMap<>();

    private final boolean allowConfigurersOfSameType;

    private BuildState buildState = BuildState.UNBUILT;

    private ObjectPostProcessor<Object> objectPostProcessor;

    protected Runnable postBuildAction = () -> {
    };
    /***
     * Creates a new instance with the provided {@link ObjectPostProcessor}. This post
     * processor must support Object since there are many types of objects that may be
     * post processed.
     *
     * @param objectPostProcessor the {@link ObjectPostProcessor} to use
     */
    protected AbstractConfiguredObjectBuilder(
            ObjectPostProcessor<Object> objectPostProcessor) {
        this(objectPostProcessor, false);
    }

    /***
     * Creates a new instance with the provided {@link ObjectPostProcessor}. This post
     * processor must support Object since there are many types of objects that may be
     * post processed.
     *
     * @param objectPostProcessor the {@link ObjectPostProcessor} to use
     * @param allowConfigurersOfSameType if true, will not override other
     * {@link ObjectConfigurer}'s when performing apply
     */
    protected AbstractConfiguredObjectBuilder(
            ObjectPostProcessor<Object> objectPostProcessor,
            boolean allowConfigurersOfSameType) {
        Assert.notNull(objectPostProcessor, "objectPostProcessor cannot be null");
        this.objectPostProcessor = objectPostProcessor;
        this.allowConfigurersOfSameType = allowConfigurersOfSameType;
    }

    /**
     * Similar to {@link #build()} and {@link #getObject()} but checks the state to
     * determine if {@link #build()} needs to be called first.
     *
     * @return the result of {@link #build()} or {@link #getObject()}. If an error occurs
     * while building, returns null.
     */
    public O getOrBuild() {
        if (isUnbuilt()) {
            try {
                return build();
            } catch (Exception e) {
                logger.debug("Failed to perform build. Returning null", e);
                return null;
            }
        } else {
            return getObject();
        }
    }

    /**
     * Applies a {@link ObjectConfigurerAdapter} to this {@link ObjectBuilder} and
     * invokes {@link ObjectConfigurerAdapter#setBuilder(ObjectBuilder)}.
     *
     * @param configurer
     * @return the {@link ObjectConfigurerAdapter} for further customizations
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <C extends ObjectConfigurerAdapter<O, B>> C apply(C configurer)
            throws Exception {
        configurer.addObjectPostProcessor(objectPostProcessor);
        configurer.setBuilder((B) this);
        add(configurer);
        return configurer;
    }

    /**
     * Applies a {@link ObjectConfigurer} to this {@link ObjectBuilder} overriding any
     * {@link ObjectConfigurer} of the exact same class. Note that object hierarchies
     * are not considered.
     *
     * @param configurer
     * @return the {@link ObjectConfigurerAdapter} for further customizations
     * @throws Exception
     */
    public <C extends ObjectConfigurer<O, B>> C apply(C configurer) throws Exception {
        add(configurer);
        return configurer;
    }

    /**
     * Sets an object that is shared by multiple {@link ObjectConfigurer}.
     *
     * @param sharedType the Class to key the shared object by.
     * @param object     the Object to store
     */
    @SuppressWarnings("unchecked")
    public <C> void setSharedObject(Class<C> sharedType, C object) {
        this.sharedObjects.put(sharedType, object);
    }

    /**
     * Gets a shared Object. Note that object heirarchies are not considered.
     *
     * @param sharedType the type of the shared Object
     * @return the shared Object or null if it is not found
     */
    @SuppressWarnings("unchecked")
    public <C> C getSharedObject(Class<C> sharedType) {
        return (C) this.sharedObjects.get(sharedType);
    }

    /**
     * Gets the shared objects
     *
     * @return the shared Objects
     */
    public Map<Class<?>, Object> getSharedObjects() {
        return Collections.unmodifiableMap(this.sharedObjects);
    }

    /**
     * Adds {@link ObjectConfigurer} ensuring that it is allowed and invoking
     * {@link ObjectConfigurer#init(ObjectBuilder)} immediately if necessary.
     *
     * @param configurer the {@link ObjectConfigurer} to add
     * @throws Exception if an error occurs
     */
    @SuppressWarnings("unchecked")
    private <C extends ObjectConfigurer<O, B>> void add(C configurer) throws Exception {
        Assert.notNull(configurer, "configurer cannot be null");

        Class<? extends ObjectConfigurer<O, B>> clazz = (Class<? extends ObjectConfigurer<O, B>>) configurer
                .getClass();
        synchronized (configurers) {
            if (buildState.isConfigured()) {
                throw new IllegalStateException("Cannot apply " + configurer
                        + " to already built object");
            }
            List<ObjectConfigurer<O, B>> configs = allowConfigurersOfSameType ? this.configurers
                    .get(clazz) : null;
            if (configs == null) {
                configs = new ArrayList<>(1);
            }
            configs.add(configurer);
            this.configurers.put(clazz, configs);
            if (buildState.isInitializing()) {
                this.configurersAddedInInitializing.add(configurer);
            }
        }
    }

    /**
     * Gets all the {@link ObjectConfigurer} instances by its class name or an empty
     * List if not found. Note that object hierarchies are not considered.
     *
     * @param clazz the {@link ObjectConfigurer} class to look for
     * @return a list of {@link ObjectConfigurer}s for further customization
     */
    @SuppressWarnings("unchecked")
    public <C extends ObjectConfigurer<O, B>> List<C> getConfigurers(Class<C> clazz) {
        List<C> configs = (List<C>) this.configurers.get(clazz);
        if (configs == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(configs);
    }

    /**
     * Removes all the {@link ObjectConfigurer} instances by its class name or an empty
     * List if not found. Note that object hierarchies are not considered.
     *
     * @param clazz the {@link ObjectConfigurer} class to look for
     * @return a list of {@link ObjectConfigurer}s for further customization
     */
    @SuppressWarnings("unchecked")
    public <C extends ObjectConfigurer<O, B>> List<C> removeConfigurers(Class<C> clazz) {
        List<C> configs = (List<C>) this.configurers.remove(clazz);
        if (configs == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(configs);
    }

    /**
     * Gets the {@link ObjectConfigurer} by its class name or <code>null</code> if not
     * found. Note that object hierarchies are not considered.
     *
     * @param clazz
     * @return the {@link ObjectConfigurer} for further customizations
     */
    @SuppressWarnings("unchecked")
    public <C extends ObjectConfigurer<O, B>> C getConfigurer(Class<C> clazz) {
        List<ObjectConfigurer<O, B>> configs = this.configurers.get(clazz);
        if (configs == null) {
            return null;
        }
        if (configs.size() != 1) {
            throw new IllegalStateException("Only one configurer expected for type "
                    + clazz + ", but got " + configs);
        }
        return (C) configs.get(0);
    }

    /**
     * Removes and returns the {@link ObjectConfigurer} by its class name or
     * <code>null</code> if not found. Note that object hierarchies are not considered.
     *
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <C extends ObjectConfigurer<O, B>> C removeConfigurer(Class<C> clazz) {
        List<ObjectConfigurer<O, B>> configs = this.configurers.remove(clazz);
        if (configs == null) {
            return null;
        }
        if (configs.size() != 1) {
            throw new IllegalStateException("Only one configurer expected for type "
                    + clazz + ", but got " + configs);
        }
        return (C) configs.get(0);
    }

    /**
     * Specifies the {@link ObjectPostProcessor} to use.
     *
     * @param objectPostProcessor the {@link ObjectPostProcessor} to use. Cannot be null
     * @return the {@link ObjectBuilder} for further customizations
     */
    @SuppressWarnings("unchecked")
    public O objectPostProcessor(ObjectPostProcessor<Object> objectPostProcessor) {
        Assert.notNull(objectPostProcessor, "objectPostProcessor cannot be null");
        this.objectPostProcessor = objectPostProcessor;
        return (O) this;
    }

    /**
     * Performs post processing of an object. The default is to delegate to the
     * {@link ObjectPostProcessor}.
     *
     * @param object the Object to post process
     * @return the possibly modified Object to use
     */
    protected <P> P postProcess(P object) {
        return this.objectPostProcessor.postProcess(object);
    }

    /**
     * Executes the build using the {@link ObjectConfigurer}'s that have been applied
     * using the following steps:
     *
     * <ul>
     * <li>Invokes {@link #beforeInit()} for any subclass to hook into</li>
     * <li>Invokes {@link ObjectConfigurer#init(ObjectBuilder)} for any
     * {@link ObjectConfigurer} that was applied to this builder.</li>
     * <li>Invokes {@link #beforeConfigure()} for any subclass to hook into</li>
     * <li>Invokes {@link #performBuild()} which actually builds the Object</li>
     * </ul>
     */
    @Override
    protected final O doBuild() throws Exception {
        synchronized (configurers) {
            buildState = BuildState.INITIALIZING;

            beforeInit();
            init();

            buildState = BuildState.CONFIGURING;

            beforeConfigure();
            configure();
            afterConfigure();

            buildState = BuildState.BUILDING;

            O result = performBuild();

            buildState = BuildState.BUILT;

            return result;
        }
    }

    protected void afterConfigure() throws Exception{
    }

    /**
     * Invoked prior to invoking each {@link ObjectConfigurer#init(ObjectBuilder)}
     * method. Subclasses may override this method to hook into the lifecycle without
     * using a {@link ObjectConfigurer}.
     */
    protected void beforeInit() throws Exception {
    }

    /**
     * Invoked prior to invoking each
     * {@link ObjectConfigurer#configure(ObjectBuilder)} method. Subclasses may
     * override this method to hook into the lifecycle without using a
     * {@link ObjectConfigurer}.
     */
    protected void beforeConfigure() throws Exception {
    }

    /**
     * Subclasses must implement this method to build the object that is being returned.
     *
     * @return the Object to be buit or null if the implementation allows it
     */
    protected abstract O performBuild() throws Exception;

    @SuppressWarnings("unchecked")
    private void init() throws Exception {
        Collection<ObjectConfigurer<O, B>> configurers = getConfigurers();

        for (ObjectConfigurer<O, B> configurer : configurers) {
            configurer.init((B) this);
        }

        for (ObjectConfigurer<O, B> configurer : configurersAddedInInitializing) {
            configurer.init((B) this);
        }
    }

    @SuppressWarnings("unchecked")
    private void configure() throws Exception {
        Collection<ObjectConfigurer<O, B>> configurers = getConfigurers();

        for (ObjectConfigurer<O, B> configurer : configurers) {
            configurer.configure((B) this);
        }
    }

    private Collection<ObjectConfigurer<O, B>> getConfigurers() {
        List<ObjectConfigurer<O, B>> result = new ArrayList<>();
        for (List<ObjectConfigurer<O, B>> configs : this.configurers.values()) {
            result.addAll(configs);
        }
        return result;
    }

    /**
     * Determines if the object is unbuilt.
     *
     * @return true, if unbuilt else false
     */
    private boolean isUnbuilt() {
        synchronized (configurers) {
            return buildState == BuildState.UNBUILT;
        }
    }

    /**
     * If the {@link ObjectConfigurer} has already been specified get the original,
     * otherwise apply the new {@link ObjectConfigurerAdapter}.
     *
     * @param configurer the {@link ObjectConfigurer} to apply if one is not found for
     *                   this {@link ObjectConfigurer} class.
     * @return the current {@link ObjectConfigurer} for the configurer passed in
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected <C extends ObjectConfigurerAdapter<O, B>> C getOrApply(
            C configurer) throws Exception {
        C existingConfig = (C) getConfigurer(configurer.getClass());
        if (existingConfig != null) {
            return existingConfig;
        }
        return apply(configurer);
    }

    /**
     * The build state for the application
     *
     * @author Rob Winch
     * @since 3.2
     */
    private enum BuildState {
        /**
         * This is the state before the {@link ObjectBuilder#build()} is invoked
         */
        UNBUILT(0),

        /**
         * The state from when {@link ObjectBuilder#build()} is first invoked until all the
         * {@link ObjectConfigurer#init(ObjectBuilder)} methods have been invoked.
         */
        INITIALIZING(1),

        /**
         * The state from after all {@link ObjectConfigurer#init(ObjectBuilder)} have
         * been invoked until after all the
         * {@link ObjectConfigurer#configure(ObjectBuilder)} methods have been
         * invoked.
         */
        CONFIGURING(2),

        /**
         * From the point after all the
         * {@link ObjectConfigurer#configure(ObjectBuilder)} have completed to just
         * after {@link AbstractConfiguredObjectBuilder#performBuild()}.
         */
        BUILDING(3),

        /**
         * After the object has been completely built.
         */
        BUILT(4);

        private final int order;

        BuildState(int order) {
            this.order = order;
        }

        public boolean isInitializing() {
            return INITIALIZING.order == order;
        }

        /**
         * Determines if the state is CONFIGURING or later
         *
         * @return
         */
        public boolean isConfigured() {
            return order >= CONFIGURING.order;
        }
    }
}
