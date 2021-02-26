package com.chua.tools.engine.example;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.MultiValueAttribute;
import com.googlecode.cqengine.attribute.SimpleAttribute;
import com.googlecode.cqengine.index.hash.HashIndex;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.index.suffix.SuffixTreeIndex;
import com.googlecode.cqengine.query.Query;
import com.googlecode.cqengine.query.option.QueryOptions;
import com.googlecode.cqengine.resultset.ResultSet;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

import static com.googlecode.cqengine.query.QueryFactory.*;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/2/26
 */
public class EngineExample {
    public static final Attribute<Car, Integer> Car_ID = attribute("carId", Car::getCarId);
    public static final Attribute<Car, String> DESCRIPTION = attribute("carDesc", Car::getManufacturer);
    public static final MultiValueAttribute<Car, String> FEATURES = attribute(String.class, "features", Car::getFeatures);


    //使用query
    public String test1() {
        StringBuffer sbBuffer = new StringBuffer();
        IndexedCollection<Car> cars = new ConcurrentIndexedCollection<Car>();
        //添加对象
        cars.add(new Car(1, "ford focus", "great condition, low mileage", Arrays.asList("spare tyre", "sunroof")));
        cars.add(new Car(2, "ford taurus", "dirty and unreliable, flat tyre", Arrays.asList("spare tyre", "radio")));
        cars.add(new Car(3, "honda civic", "has a flat tyre and high mileage", Arrays.asList("radio")));
        //添加索引

        cars.addIndex(NavigableIndex.onAttribute(Car_ID));
        cars.addIndex(SuffixTreeIndex.onAttribute(DESCRIPTION));
        cars.addIndex(HashIndex.onAttribute(FEATURES));

        //创建查询
        Query<Car> query1 = and(startsWith(DESCRIPTION, "great"), lessThan(Car_ID, 4));
        //获取查询结果
        ResultSet<Car> result = cars.retrieve(query1);
        if (result.isNotEmpty()) {
            result.forEach(e -> {
                sbBuffer.append(e.toString());
                sbBuffer.append("\n");
            });
            return sbBuffer.toString();
        } else {
            return "nothing found";
        }
    }

    @Data
    private static class Car {
        public static final SimpleAttribute<Car, Integer> CAR_ID = new SimpleAttribute<Car, Integer>("carId") {
            @Override
            public Integer getValue(Car car, QueryOptions queryOptions) { return car.carId; }
        };

        public static final SimpleAttribute<Car, String> MANUFACTURER = new SimpleAttribute<Car, String>("manufacturer") {
            @Override
            public String getValue(Car car, QueryOptions queryOptions) { return car.manufacturer; }
        };

        public static final SimpleAttribute<Car, String> MODEL = new SimpleAttribute<Car, String>("model") {
            @Override
            public String getValue(Car car, QueryOptions queryOptions) { return car.model; }
        };

        public static final MultiValueAttribute<Car, String> FEATURES = new MultiValueAttribute<Car, String>("features") {
            @Override
            public Iterable<String> getValues(Car car, QueryOptions queryOptions) { return car.features; }
        };


        public enum Color {RED, GREEN, BLUE, BLACK, WHITE}
        final int carId;
        final String manufacturer;
        final String model;
        final List<String> features;

        public Car(int carId, String manufacturer, String model, List<String> features) {
            this.carId = carId;
            this.manufacturer = manufacturer;
            this.model = model;
            this.features = features;
        }
    }
}
