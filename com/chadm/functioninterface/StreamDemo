import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class StreamDemo {

    static String [] strStream = {"hello","world"};
    static int [] intStream = {1,2,3,6,2,4};

    public static void main(String[] args) {

        limitAndSkip();

        mapAndFlatMap();

        filterAndDistinct();

        findAndMatch();

        reduceAndMaxMin();

        List<String> list = Arrays.stream(strStream)
                .map(String::toUpperCase)
                // 把流归约成一个集合，如List/Map
                .collect(toList());
        System.out.println("collect list = "+ list);

        final Map<Character, List<String>> names = Stream.of("Max", "Min", "Reduce", "Filter")
                // 用 groupingBy 对流中元素进行分组。分组时对流中所有元素应用同一个 Function。
                // 具有相同结果的元素被分到同一组。分组之后的结果是一个 Map，
                // Map 的键是应用 Function 之后的结果，而对应的值是属于该组的所有元素的 List
                .collect(Collectors.groupingBy(v -> v.charAt(0)));
        System.out.println(names);
    }

    static void reduceAndMaxMin() {
        /*
         * Optional<T> reduce(BinaryOperator<T> accumulator) 等效如下代码
         * {
         *     boolean foundAny = false;
         *     T result = null;
         *     for (T element : this stream) {
         *         if (!foundAny) {
         *            foundAny = true;
         *            result = element;
         *         } else
         *            result = accumulator.apply(result, element);
         *     }
         *     return foundAny ? Optional.of(result) : Optional.empty();
         * }
         */
        OptionalInt sum1 = Arrays.stream(intStream)
                .reduce((i,j) -> i+j);
        sum1.ifPresent(System.out::println);

        /*  T reduce(T identity, BinaryOperator<T> accumulator) 等效于
         *  {
         *      U result = identity;
         *      for (T element : this stream)
         *          result = accumulator.apply(result, element)
         *      return result;
         *   }
         */
        int sum2 = Arrays.stream(intStream)
                .reduce(0,(i,j) -> i+j);
        System.out.println(sum2);

        /**
         * <U> U reduce(U identity,
         *                  BiFunction<U, ? super T, U> accumulator,
         *                  BinaryOperator<U> combiner);
         *
         * 等效如下代码
         * {
         *      U result = identity;
         *      for (T element : this stream)
         *          result = accumulator.apply(result, element)
         *      return result;
         * }
         * 但并不被限制按顺序执行，可能并发执行，combiner必须兼容accumulator
         * combiner.apply(u, accumulator.apply(identity, t)) == accumulator.apply(u, t)
         */
        int sum3 = Stream.of(1, 2, 3, 4, 5)
                .parallel()
                .reduce(0, (i, j) -> i + j, (i, j) -> i + j);
        System.out.println(sum3);

        // max求流中元素的最大值
        OptionalInt max = Arrays.stream(intStream)
                .max();
        max.ifPresent(System.out::println);

        // min求流中元素的最小值
        OptionalInt min = Arrays.stream(intStream)
                .min();
        min.ifPresent(System.out::println);

        // 返回流中元素的数量
        long count = Arrays.stream(intStream)
                .count();
        System.out.println(count);
    }

    static void limitAndSkip() {
        // 截断流使其最多只包含指定数量的元素
        Arrays.stream(intStream)
                .limit(2)
                .forEach(System.out::println);
        // 返回一个新的流，并跳过原始流中的前 N 个元素
        Arrays.stream(intStream)
                .skip(2)
                .forEach(System.out::println);
    }

    static void findAndMatch() {
        boolean anyMatch = Arrays.stream(strStream)
                .anyMatch(v -> v.equals("hello"));
        System.out.println("anyMatch = "+ anyMatch);

        boolean allMatch = Arrays.stream(strStream)
                .allMatch(v -> v.equals("hello"));
        System.out.println("allMatch = "+ allMatch);

        boolean noneMatch = Arrays.stream(strStream)
                .noneMatch(v -> v.equals("hello"));
        System.out.println("noneMatch = "+ noneMatch);

        // findFirst在有相遇顺序的流中，总是返回的第一个；在没有相遇顺序的流中，每次可能返回的值不一样
        OptionalInt firstValue= Arrays.stream(intStream)
                .filter(i -> i % 3 ==0)
                .findFirst();
        firstValue.ifPresent(System.out::println);

        // 在并发场景下，使用findAny的限制比findFirst要少
        OptionalInt anyValue= Arrays.stream(intStream)
                .filter(i -> i % 3 ==0)
                .findAny();
        anyValue.ifPresent(System.out::println);
    }

    static void filterAndDistinct() {
        // filter 过滤流中的元素，只保留满足由 Predicate 所指定的条件的元素
        Arrays.stream(intStream)
                .filter(i -> i % 2 == 0)
                .forEach(System.out::println);

        // 使用equals方法来删除流中的重复元素
        Arrays.stream(intStream)
                .distinct()
                .forEach(System.out::println);
    }

    static void mapAndFlatMap() {
        // map 一对一的映射关系
        long count = Arrays.stream(strStream)
                .map(String::toLowerCase)
                .count();
        System.out.println("count = " + count);

        // flatMap 支持一对多的映射关系
        Stream.of(1,2,3)
                .map(v -> v+1)
                .flatMap(v -> Stream.of(v*5,v*10))
                .forEach(System.out::println);

        // flatMap 把Stream<String []> 转换成Stream<String> ,然后合并成一个Stream返回
        List<String> uniqueChar = Arrays.stream(strStream)
                .map(s -> s.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
        System.out.println("uniqueChar list = "+ uniqueChar);
    }

}
