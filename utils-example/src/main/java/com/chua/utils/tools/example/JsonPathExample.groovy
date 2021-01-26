package com.chua.utils.tools.example

import com.jayway.restassured.path.json.JsonPath;


/**
 * @author CH* @version 1.0.0* @since 2021/1/26
 */
class JsonPathExample {

    static void main(String[] args) {
        def object = JsonPath.from(
                '''
                        {
                        "store": {
                            "book": [
                                {
                                    "category": "reference",
                                    "author": "Nigel Rees",
                                    "title": "Sayings of the Century",
                                    "price": 8.95
                                },
                                {
                                    "category": "fiction",
                                    "author": "Evelyn Waugh",
                                    "title": "Sword of Honour",
                                    "price": 12.99
                                },
                                {
                                    "category": "fiction",
                                    "author": "Herman Melville",
                                    "title": "Moby Dick",
                                    "isbn": "0-553-21311-3",
                                    "price": 8.99
                                },
                                {
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
                    '''
        ).get("store.book.category")

        println object
    }
}
