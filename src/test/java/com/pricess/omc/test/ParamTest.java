package com.pricess.omc.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.pricess.omc.annotation.RequestBody;
import com.pricess.omc.param.HandlerObject;
import com.pricess.omc.param.ObjectParameter;
import com.pricess.omc.validator.ParamAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class ParamTest implements ParamAdapter {

    @RequestBody
    private List<Order> orders;

    private String paramName;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public class Order {
        private String name;

        private String order;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        ParamTest paramTest = ParamTest.class.newInstance();

        String date = "2020-05-15";

        ObjectMapper objectMapper = new ObjectMapper();

        HandlerObject objectParameter = new HandlerObject(ParamTest.class, null);

        for (Field field : paramTest.getClass().getDeclaredFields()) {


            RequestBody requestBody = field.getAnnotation(RequestBody.class);

            JavaType javaType = getJavaType(field.getGenericType());
            field.setAccessible(true);
            System.err.println(javaType);
        }
    }

    protected static JavaType getJavaType(Type type) {
        ObjectMapper objectMapper = new ObjectMapper();

        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return typeFactory.constructType(type);
    }

}
