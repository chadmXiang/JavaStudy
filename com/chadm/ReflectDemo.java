import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectDemo {

    public static void main(String[] args) {

        // 获取Class对象
        Class aClass = ReflectObject.class;
        // 打印所有定义的方法，getDeclaredMethods能获取private方法，getMethods不能
        Method [] methods = aClass.getDeclaredMethods();
        Arrays.stream(methods).forEach(System.out::println);
        // 打印所有定义的属性，getDeclaredFields能获取private属性，getFields不能
        Field[] fields = aClass.getDeclaredFields();
        Arrays.stream(fields).forEach(System.out::println);

        ReflectObject object = null;

        try {
            // 通过Constructor构造对象实例
            Constructor constructor = aClass.getDeclaredConstructor(new Class[]{String.class,int.class});
            object = (ReflectObject) constructor.newInstance("",0);

            // 反射私有方法
            Method setName = aClass.getDeclaredMethod("setName",new Class[]{String.class});
            // 修改访问能力
            setName.setAccessible(true);
            setName.invoke(object,"chadm");

            // 反射私有属性
            Field name = aClass.getDeclaredField("name");
            // 修改访问能力
            name.setAccessible(true);
            System.out.println(name.get(object));
            name.set(object,"Achadm");
            System.out.println(name.get(object));

            // 反射public方法
            Method getAge = aClass.getDeclaredMethod("getAge",null);
            int age = (int) getAge.invoke(object,null);
            System.out.println(age);

            // 反射public属性
            Field score = aClass.getDeclaredField("score");
            System.out.println(score.get(object));
            score.set(object,10);
            System.out.println(score.get(object));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


}
