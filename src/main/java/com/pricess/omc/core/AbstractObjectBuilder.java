package com.pricess.omc.core;


import com.pricess.omc.exception.ObjectBuiltException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A base {@link ObjectBuilder} that ensures the object being built is only built one
 * time.
 *
 * @param <O> the type of Object that is being built
 *
 * @author 王晟权
 *
 */
public abstract class AbstractObjectBuilder<O> implements ObjectBuilder<O> {
	private AtomicBoolean building = new AtomicBoolean();

	private O object;

	@Override
	public final O build() throws Exception {
		if (this.building.compareAndSet(false, true)) {
			this.object = doBuild();
			return this.object;
		}
		throw new ObjectBuiltException("This object has already been built");
	}

	/**
	 * Gets the object that was built. If it has not been built yet an Exception is
	 * thrown.
	 *
	 * @return the Object that was built
	 */
	public final O getObject() {
		if (!this.building.get()) {
			throw new IllegalStateException("This object has not been built");
		}
		return this.object;
	}

	/**
	 * Subclasses should implement this to perform the build.
	 *
	 * @return the object that should be returned by {@link #build()}.
	 *
	 * @throws Exception if an error occurs
	 */
	protected abstract O doBuild() throws Exception;
}
