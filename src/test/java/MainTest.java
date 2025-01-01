import org.example.JavaBreakpointLocation;
import org.example.KotlinPositionFinder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void test5() {
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
        JavaBreakpointLocation location = kotlinPositionFinder.locateBreakpoint(6, -1);
        assertEquals("Person", location.className());
        assertEquals("init", location.methodName());

        JavaBreakpointLocation location1 = kotlinPositionFinder.locateBreakpoint(12, -1);
        assertEquals("Person", location1.className());
        assertEquals("constructor", location1.methodName());
    }

    @Test
    public void test4() {
        KotlinPositionFinder kotlinPositionFinder = new KotlinPositionFinder("""
fun forEachControl() {
    listOf(1, 2, 3, 4, 5).forEach{
        if (it == 3) return
        println("it:$it")
    }
    println("循环外继续执行")
}
    """);
        JavaBreakpointLocation location = kotlinPositionFinder.locateBreakpoint(4, -1);
        assertEquals("", location.className());
        assertEquals("forEachControl", location.methodName());

        JavaBreakpointLocation location1 = kotlinPositionFinder.locateBreakpoint(6, -1);
        assertEquals("", location1.className());
        assertEquals("forEachControl", location1.methodName());
    }

    @Test
    public void test3() {
        KotlinPositionFinder kotlinPositionFinder = new KotlinPositionFinder("""
class OuterClass {
    private val outerProperty = "Outer property";
    inner class InnerClass {
        fun accessOuterProperty() {
            println(outerProperty);
        }
    }
}

val innerClassInstance = OuterClass().InnerClass();
val s = innerClassInstance.accessOuterProperty();
    """);
        JavaBreakpointLocation location = kotlinPositionFinder.locateBreakpoint(6, -1);
        assertEquals("InnerClass", location.className());
        assertEquals("accessOuterProperty", location.methodName());

        JavaBreakpointLocation location1 = kotlinPositionFinder.locateBreakpoint(11, -1);
        assertEquals("", location1.className());
        assertEquals("", location1.methodName());
    }

    @Test
    public void test2() {
        KotlinPositionFinder kotlinPositionFinder = new KotlinPositionFinder("""
    val helloFun: () -> String = {
        var a = 1;
        "Hello World"
    }
    var c = 3;
    """);
        JavaBreakpointLocation location = kotlinPositionFinder.locateBreakpoint(2, -1);
        assertEquals("", location.className());
        assertEquals("helloFun", location.methodName());

        JavaBreakpointLocation location1 = kotlinPositionFinder.locateBreakpoint(5, -1);
        assertEquals("", location1.className());
        assertEquals("", location1.methodName());
    }

    @Test
    public void test1() {
        KotlinPositionFinder kotlinPositionFinder = new KotlinPositionFinder("""
                class B {
                     fun foo(i: Int) {
                        var a = 2;
                    }
                }
                fun double(x: Int): Int {
                    return 2 * x
                }
                """);
        JavaBreakpointLocation location = kotlinPositionFinder.locateBreakpoint(3, -1);
        assertEquals("B", location.className());
        assertEquals("foo", location.methodName());

        JavaBreakpointLocation location1 = kotlinPositionFinder.locateBreakpoint(8, -1);

        assertEquals("", location1.className());
        assertEquals("double", location1.methodName());
    }
}