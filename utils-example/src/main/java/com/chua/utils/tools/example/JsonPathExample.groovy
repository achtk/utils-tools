package com.chua.utils.tools.example


import com.chua.utils.tools.collects.FlatMap
import com.chua.utils.tools.function.JsonPath
import com.chua.utils.tools.util.JsonUtils

/**
 * @author CH* @version 1.0.0* @since 2021/1/26
 */
class JsonPathExample {

    static void main(String[] args) {
        JsonPath jsonPath = JsonUtils.parser(
                """
                    { 
                    "store": { 
                        "book": [ 
                            { 
                                "category": "reference", 
                                "author": "igel Rees", 
                                "title": "Sayings of the Century", 
                                "price": 8.95 
                            }, { 
                                "category": "fiction", 
                                "author": "Evelyn Waugh", 
                                "isbn": "0-553-21311-3", 
                                "title": "Sword of Honour", 
                                "price": 12.99 
                            }, { 
                                "category": "fiction", 
                                "author": "Herman Melville", 
                                "title": "Moby Dick", 
                                "price": 8.99 
                            }, { 
                                "category": "fiction", 
                                "author": "J. R. R. Tolkien", 
                                "title": "The Lord of the Rings", 
                                "isbn": "0-395-19395-8", 
                                "price": 22.99 
                            } 
                        ], 
                        "bicycle": { 
                            "color": "red", 
                            "price": 19.95 
                        } 
                    }, 
                     "expensive": 10 
                    }
                """
        )

        List<Object> objects = jsonPath.find("store.*.price")
        Map<String, Object> map = jsonPath.findMap("store.*.price")
        FlatMap flatMap = jsonPath.flatMap("*")

        System.out.println(objects)
        System.out.println(map)
        System.out.println(flatMap)
    }
}
