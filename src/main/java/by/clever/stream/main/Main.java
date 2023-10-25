package by.clever.stream.main;

import by.clever.stream.main.model.Animal;
import by.clever.stream.main.model.Car;
import by.clever.stream.main.model.Examination;
import by.clever.stream.main.model.Flower;
import by.clever.stream.main.model.House;
import by.clever.stream.main.model.Person;
import by.clever.stream.main.model.Student;
import by.clever.stream.main.util.Util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        task1();
        task2();
        task3();
        task4();
        task5();
        task6();
        task7();
        task8();
        task9();
        task10();
        task11();
        task12();
        task13();
        task14();
        task15();
        task16();
        task17();
        task18();
        task19();
        task20();
        task21();
        task22();
    }

    public static void task1() {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> animal.getAge() > 9 && animal.getAge() < 21)
                .sorted(Comparator.comparing(Animal::getAge))
                .toList()
                .subList(14, 21)
                .forEach(System.out::println);
    }

    public static void task2() {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> "Japanese".equals(animal.getOrigin()))
                .peek(animal -> {
                    if ("Female".equals(animal.getGender())) {
                        String breedUpperCase = animal.getBread().toUpperCase();
                        animal.setBread(breedUpperCase);
                    }
                })
                .forEach(System.out::println);
    }

    public static void task3() {
        List<Animal> animals = Util.getAnimals();
        animals.stream()
                .filter(animal -> animal.getAge() > 30)
                .map(Animal::getOrigin)
                .distinct()
                .filter(origin -> origin.startsWith("A"))
                .forEach(System.out::println);
    }

    public static void task4() {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .filter(animal -> "Female".equals(animal.getGender()))
                .count());
    }

    public static void task5() {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .filter(animal -> animal.getAge() > 19 && animal.getAge() < 31)
                .anyMatch(animal -> "Hungarian".equals(animal.getOrigin())));
    }

    public static void task6() {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .allMatch(animal -> "Male".equals(animal.getGender()) || "Female".equals(animal.getGender())));
    }

    public static void task7() {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .anyMatch(animal -> "Oceania".equals(animal.getOrigin())));
    }

    public static void task8() {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .sorted(Comparator.comparing(Animal::getBread))
                .limit(100)
                .max(Comparator.comparing(Animal::getAge))
                .get()
                .getAge());
    }

    public static void task9() {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .map(Animal::getBread)
                .map(String::toCharArray)
                .sorted(Comparator.comparing(chars -> chars.length))
                .findFirst()
                .get()
                .length);
    }

    public static void task10() {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .mapToInt(Animal::getAge)
                .sum());
    }

    public static void task11() {
        List<Animal> animals = Util.getAnimals();
        System.out.println(animals.stream()
                .filter(animal -> "Indonesian".equals(animal.getOrigin()))
                .mapToDouble(Animal::getAge)
                .average()
                .getAsDouble());
    }

    public static void task12() {
        List<Person> persons = Util.getPersons();
        LocalDate maxBirthDate = LocalDate.now().minusYears(18);
        LocalDate minBirthDate = LocalDate.now().minusYears(27);

        persons.stream()
                .filter(person -> "Male".equals(person.getGender()))
                .filter(person -> person.getDateOfBirth().isAfter(minBirthDate) && person.getDateOfBirth().isBefore(maxBirthDate))
                .sorted(Comparator.comparing(Person::getRecruitmentGroup))
                .limit(200)
                .forEach(System.out::println);
    }

    public static void task13() {
        List<House> houses = Util.getHouses();
        LocalDate childAge = LocalDate.now().minusYears(18);
        LocalDate maleRetireAge = LocalDate.now().minusYears(63);
        LocalDate femaleRetireAge = LocalDate.now().minusYears(58);

        houses.stream()
                .flatMap(house -> house.getPersonList().stream()
                        .map(person -> Map.entry(((Supplier<Integer>) () -> {
                            if ("Hospital".equals(house.getBuildingType())) {
                                return 1;
                            }
                            if (person.getDateOfBirth().isAfter(childAge) ||
                                    ("Male".equals(person.getGender()) && person.getDateOfBirth().isBefore(maleRetireAge)) ||
                                    ("Female".equals(person.getGender()) && person.getDateOfBirth().isBefore(femaleRetireAge))) {
                                return 2;
                            }
                            return 3;
                        }).get(), person)))
                .sorted(Map.Entry.comparingByKey())
                .limit(500)
                .forEach(System.out::println);
    }

    public static void task14() {
        List<Car> cars = Util.getCars();
        Map<Integer, Predicate<Car>> predicates = createPredicates();
        BigDecimal oneTonPriceDollar = new BigDecimal("7.14");

        System.out.println(cars.stream().map(car -> Map.entry(predicates.entrySet().stream()
                        .filter(entry -> entry.getValue().test(car))
                        .findFirst()
                        .get()
                        .getKey(), car))
                .filter((entry) -> entry.getKey() != 7)
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingDouble(entry -> oneTonPriceDollar
                        .multiply(BigDecimal.valueOf(entry.getValue().getMass()))
                        .doubleValue())))
                .values()
                .stream()
                .mapToDouble(v -> v)
                .sum());
    }

    public static void task15() {
        List<Flower> flowers = Util.getFlowers();
        List<String> vaseMaterials = List.of("Glass", "Aluminum", "Steel");
        BigDecimal fiveYearWaterConsumptionPrice = new BigDecimal("1.39")
                .multiply(BigDecimal.valueOf(12))
                .multiply(BigDecimal.valueOf(5));

        System.out.println(flowers.stream()
                .sorted(Comparator.comparing(Flower::getOrigin).reversed()
                        .thenComparing(Flower::getPrice)
                        .thenComparing(Flower::getWaterConsumptionPerDay).reversed())
                .filter(flower -> flower.getCommonName().charAt(0) <= 'S'
                        && flower.getCommonName().charAt(0) >= 'C')
                .filter(Flower::isShadePreferred)
                .filter(flower -> flower.getFlowerVaseMaterial().stream()
                        .anyMatch(vaseMaterials::contains))
                .collect(Collectors.groupingBy(Flower::getCommonName,
                        Collectors.summingDouble(flower -> BigDecimal.valueOf(flower.getPrice())
                                .add(fiveYearWaterConsumptionPrice)
                                .doubleValue())))
                .values()
                .stream()
                .mapToDouble(value -> value)
                .sum());
    }

    public static void task16() {
        List<Student> students = Util.getStudents();
        students.stream()
                .filter(student -> student.getAge() < 18)
                .sorted(Comparator.comparing(Student::getSurname))
                .forEach(System.out::println);
    }

    public static void task17() {
        List<Student> students = Util.getStudents();
        students.stream()
                .map(Student::getGroup)
                .distinct()
                .forEach(System.out::println);
    }

    public static void task18() {
        List<Student> students = Util.getStudents();
        students.stream()
                .collect(Collectors.groupingBy(Student::getFaculty, Collectors.averagingDouble(Student::getAge)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(System.out::println);
    }

    public static void task19() {
        List<Examination> examinations = Util.getExaminations();
        List<Student> students = Util.getStudents();
        String group = "M-2";

        examinations.stream()
                .filter(examination -> examination.getExam3() > 4)
                .map(Examination::getStudentId)
                .forEach(studentID -> {
                    for (Student student : students) {
                        if (student.getId() == studentID && group.equals(student.getGroup())) {
                            System.out.println(student);
                        }
                    }
                });
    }

    public static void task20() {
        List<Student> students = Util.getStudents();
        List<Examination> examinations = Util.getExaminations();
        System.out.println(students.stream()
                .collect(Collectors.groupingBy(Student::getFaculty, Collectors.collectingAndThen(Collectors.toList(), list -> {

                                            List<Integer> listID = list.stream()
                                                    .map(Student::getId)
                                                    .toList();

                                            List<Integer> exam1Mark = new ArrayList<>();

                                            for (int k = 0; k < listID.size(); k++) {
                                                for (int n = 0; n < examinations.size(); n++) {
                                                    if (listID.get(k) == examinations.get(n).getStudentId()) {
                                                        exam1Mark.add(examinations.get(n).getExam1());
                                                    }
                                                }
                                            }

                                            return exam1Mark.stream()
                                                    .mapToDouble(mark -> mark)
                                                    .average()
                                                    .getAsDouble();
                                        }
                                )
                        )
                )
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get());
    }

    public static void task21() {
        List<Student> students = Util.getStudents();
        students.stream()
                .collect(Collectors.toMap(Student::getGroup,
                        student -> 1,
                        Integer::sum,
                        TreeMap::new))
                .entrySet()
                .stream()
                .forEach(System.out::println);

    }

    public static void task22() {
        List<Student> students = Util.getStudents();
        System.out.println(students.stream()
                .collect(Collectors.groupingBy(Student::getFaculty, Collectors.collectingAndThen(Collectors.toList(), list -> {

                                            return list.stream()
                                                    .mapToInt(Student::getAge)
                                                    .min()
                                                    .getAsInt();
                                        }
                                )
                        )
                )
        );
    }

    private static Map<Integer, Predicate<Car>> createPredicates() {

        Map<Integer, Predicate<Car>> predicates = new HashMap<>();

        predicates.put(1, (car) -> "Jaguar".equals(car.getCarMake()) ||
                "White".equals(car.getColor()));
        predicates.put(2, (car) -> car.getMass() < 1500 &&
                ("BMW".equals(car.getCarMake()) ||
                        "Lexus".equals(car.getCarMake()) ||
                        "Chrysler".equals(car.getCarMake()) ||
                        "Toyota".equals(car.getCarMake())));
        predicates.put(3, (car) -> "GMC".equals(car.getCarMake()) ||
                "Dodge".equals(car.getCarMake()) ||
                ("Red".equals(car.getColor()) && car.getMass() > 4000));
        predicates.put(4, (car) -> "Civic".equals(car.getCarModel()) ||
                "Cherokee".equals(car.getCarModel()) ||
                car.getReleaseYear() < 1982);
        predicates.put(5, (car) -> !("Yellow".equals(car.getColor()) ||
                "Red".equals(car.getColor()) ||
                "Green".equals(car.getColor()) ||
                "Blue".equals(car.getColor())) ||
                car.getPrice() > 40000);
        predicates.put(6, (car) -> car.getVin() != null &&
                car.getVin().contains("59"));
        predicates.put(7, (car) -> !predicates.get(1).test(car)
                && !predicates.get(2).test(car)
                && !predicates.get(3).test(car)
                && !predicates.get(4).test(car)
                && !predicates.get(5).test(car)
                && !predicates.get(6).test(car));

        return predicates;
    }
}
