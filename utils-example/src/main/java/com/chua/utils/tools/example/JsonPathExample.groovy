package com.chua.utils.tools.example


import com.chua.utils.tools.collects.FlatMap
import com.chua.utils.tools.function.JsonPath
import com.chua.utils.tools.util.JsonUtils
import org.springframework.web.reactive.function.client.WebClient

/**
 * @author CH* @version 1.0.0* @since 2021/1/26
 */
class JsonPathExample {

    static void main(String[] args) {
        JsonPath jsonPath = JsonUtils.parser(getDemo1())

        def text = '*.*.price' as String
        List<Object> objects = jsonPath.find(text)
        Map<String, Object> map = jsonPath.findMap(text)
        FlatMap flatMap = jsonPath.flatMap(text)

        System.out.println(objects)
        System.out.println(map)
        System.out.println(flatMap)

        WebClient
    }

    static String getDemo1() {
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
    }

    static String getDemo() {
        """
                    {
                       "min_position": 7,
                       "has_more_items": true,
                       "items_html": "Car",
                       "new_latent_count": 2,
                       "data": {
                          "length": 20,
                          "text": "QQE2.com",
                          "datas": [
                             {
                                "min_position": 4,
                                "category": false,
                                "items_html": "Car",
                                "price": 1
                             },
                             {
                                "min_position": 9,
                                "category": true,
                                "items_html": "Bike",
                                "price": 1
                             },
                             {
                                "min_position": 7,
                                "category": false,
                                "items_html": "Car",
                                "price": 1
                             },
                             {
                                "min_position": 7,
                                "category": true,
                                "items_html": "Bike",
                                "price": 8
                             },
                             {
                                "min_position": 6,
                                "category": true,
                                "items_html": "Car",
                                "price": 4
                             }
                          ]
                       },
                       "numericalArray": [
                          24,
                          24,
                          32,
                          24,
                          33
                       ],
                       "StringArray": [
                          "Oxygen",
                          "Nitrogen",
                          "Carbon",
                          "Carbon",
                          "Carbon"
                       ],
                       "multipleTypesArray": 3,
                       "objArray": [
                          {
                             "class": "middle",
                             "age": 5
                          },
                          {
                             "class": "lower",
                             "age": 5
                          },
                          {
                             "class": "lower",
                             "age": 1
                          },
                          {
                             "class": "middle",
                             "age": 7
                          },
                          {
                             "class": "upper",
                             "age": 4
                          },
                          {
                             "class": "middle",
                             "age": 1
                          },
                          {
                             "class": "middle",
                             "age": 3
                          },
                          {
                             "class": "middle",
                             "age": 3
                          },
                          {
                             "class": "lower",
                             "age": 6
                          },
                          {
                             "class": "middle",
                             "age": 1
                          }
                       ]
                    }
                """
    }
}
