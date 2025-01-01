package org.example;


public class Main {
    public static void main(String[] args) {
        KotlinPositionFinder kotlinPositionFinder = new KotlinPositionFinder("""
class Person(val name: String) {
    var age: Int = 0;

    // 主构造函数已经定义了 name 属性
    init {
        println("Person initialized with name: $name");
    }

    // 次构造函数，需要委托给主构造函数
    constructor(name: String, age: Int) : this(name) {
        this.age = age;
        println("Person initialized with name: $name and age:$age");
    }
}

// 使用主构造函数创建对象
val person1 = Person("Alice");

// 使用次构造函数创建对象
val person2 = Person("Bob", 30);
""");
//        kotlinPositionFinder.locateBreakpoint(6, -1).print();
        kotlinPositionFinder.locateBreakpoint(12, -1).print();
    }
}