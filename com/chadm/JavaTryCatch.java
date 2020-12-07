public class JavaTryCatch {

    static class Person {
        public int age;
    }

    public static void main(String[] args) {
        Person person = new Person();
        /*
         * 不管try/catch里面是否有return,finally里面的代码肯定会执行
         *
         * 不管有没有异常，finally里面的方法肯定会执行
         *
         * 如果在finally里面有return语句，会覆盖在try/catch里面的返回值，所以不建议在finally里面调用return
         *
         * finally里面有改变返回值的且没有return的场景
         *   1.如果是返回的基础变量，不影响在try/catch里面的返回值
         *   2.如果是返回的对象引用，在finally里有改变对象的属性，则会影响在try/catch里面的返回值
         */
        int value = finallyWithoutReturn();
        System.out.println("finallyWithoutReturn()函数返回1：" + (value == 1));

        value = finallyWithoutReturn(person).age;
        System.out.println("finallyWithoutReturn(person)函数返回2：" + (value == 2));

        value = finallyWithReturn();
        System.out.println("finallyWithReturn()函数返回2：" + (value == 2));

        value = finallyWithReturn(person).age;
        System.out.println("finallyWithReturn(person)函数返回2：" + (value == 2));

        value = catchAndFinallyReturn();
        System.out.println("catchAndFinallyReturn()函数返回1：" + (value == 1));
    }

    /**
     * finally里面有改变返回值的场景下,如果是返回的对象引用，在finally里有改变对象的属性，则会影响在try/catch里面的返回值
     *
     * @param person A Person object instance
     * @return 2
     */
    private static Person finallyWithoutReturn(Person person){
        int i = 0;
        try {
            person.age = ++i;
            return person;
        }catch (Exception e){
            person.age = i;
            return person;
        }finally {
            person.age = ++i;
        }
    }

    /**
     * finally里面有改变返回值的场景下,如果是返回的基础变量，不影响在try/catch里面的返回值
     *
     * @return 1
     */
    private static int finallyWithoutReturn(){
        int i = 0;
        try {
            return ++i;
        }catch (Exception e){
            return -1;
        }finally {
            System.out.println("finallyWithoutReturn finally modify int value: " + ++i);
        }
    }

    /**
     * finally里面有改变返回值的场景下,如果是返回的对象引用，在finally里有改变对象的属性，则会影响在try/catch里面的返回值
     *
     * @param person A Person object instance
     * @return 2
     */
    private static Person finallyWithReturn(Person person){
        int i = 0;
        try {
            person.age = ++i;
            return person;
        }catch (Exception e){
            person.age = -1;
            return person;
        }finally {
            // final 里面的代码肯定会执行，使用return 会改变try里面的值
            System.out.println("finallyWithReturn finally modify person age: " + ++i);
            person.age = i;
            return person;
        }
    }

    /**
     * finally里面有改变返回值的场景下,finally里面的return会覆盖在try/catch里面的返回值
     *
     * @return 2
     */
    private static int finallyWithReturn(){
        int i = 0;
        try {
            return ++i;
        }catch (Exception e){
            return -1;
        }finally {
            System.out.println("finallyWithReturn finally return int value: " + ++i);
            return i;
        }
    }

    /**
     * 不管有没有异常，finally里面的方法肯定会执行
     *
     * finally里面有改变返回值的场景下,在finally里面有return语句，如果是返回的基础变量，不影响在try/catch里面的返回值
     *
     * @return 1
     */
    private static int catchAndFinallyReturn(){
        int i = 0;
        try {
            throw new Exception();
        }catch (Exception e){
            return -1;
        }finally {
            // final 里面的代码肯定会执行，使用return 会改变catch里面的值
            System.out.println("catchAndFinallyReturn finally modify int value: " + ++i);
            return i;
        }
    }


}
