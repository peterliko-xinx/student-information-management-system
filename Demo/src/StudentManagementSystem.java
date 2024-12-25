import java.io.*;
import java.util.*;

public class StudentManagementSystem {
    private static List<Student> students = new ArrayList<>();
    private static final String FILE_PATH = "students.txt";

    public static void main(String[] args) {
        loadStudents(); // 加载学生数据
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n学生信息管理系统");
            System.out.println("1. 导入学生信息");
            System.out.println("2. 添加学生");
            System.out.println("3. 删除学生");
            System.out.println("4. 修改学生信息");
            System.out.println("5. 查找学生");
            System.out.println("6. 筛选学生");
            System.out.println("7. 排序学生");
            System.out.println("8. 按姓名折半查找");
            System.out.println("9. 按学号折半查找");
            System.out.println("10. 退出");
            System.out.print("请选择操作: ");

            int choice = sc.nextInt();
            sc.nextLine(); // 消耗换行符
            switch (choice) {
                case 1 -> importStudents();
                case 2 -> addStudent();
                case 3 -> deleteStudent();
                case 4 -> modifyStudent();
                case 5 -> findStudent();
                case 6 -> filterStudents();
                case 7 -> sortStudents();
                case 8 -> binarySearchByName();
                case 9 -> binarySearchById();
                case 10 -> {
                    System.out.println("退出系统！");
                    return;
                }
                default -> System.out.println("无效选择，请重新输入！");
            }
        }
    }

    private static void loadStudents() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                if (data.length == 5) {
                    students.add(new Student(data[0], data[1], data[2], data[3], Integer.parseInt(data[4])));
                }
            }
        } catch (IOException e) {
            System.out.println("加载学生信息失败: " + e.getMessage());
        }
    }

    private static void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Student student : students) {
                writer.write(student.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("保存学生信息失败: " + e.getMessage());
        }
    }

    private static void importStudents() {
        loadStudents();
        System.out.println("学生信息导入成功！");
    }

    private static void addStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入班级: ");
        String studentClass = sc.nextLine().trim();

        String studentId;
        while (true) {
            System.out.print("请输入学号 (11 位): ");
            studentId = sc.nextLine().trim();
            if (studentId.length() == 11) break;
            System.out.println("学号必须为 11 位！");
        }

        System.out.print("请输入姓名: ");
        String name = sc.nextLine().trim();

        String gender;
        while (true) {
            System.out.print("请输入性别 (男/女): ");
            gender = sc.nextLine().trim();
            if (gender.equals("男") || gender.equals("女")) break;
            System.out.println("性别只能是 '男' 或 '女'！");
        }

        int age;
        while (true) {
            System.out.print("请输入年龄: ");
            age = sc.nextInt();
            if (age >= 0) break;
            System.out.println("年龄必须大于等于 0！");
        }

        students.add(new Student(studentClass, studentId, name, gender, age));
        saveStudents();
        System.out.println("学生信息添加成功！");
    }

    private static void deleteStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入要删除的学号: ");
        String studentId = sc.nextLine().trim();

        students.removeIf(student -> student.getStudentId().equals(studentId));
        saveStudents();
        System.out.println("学生删除成功！");
    }

    private static void modifyStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入要修改的学号: ");
        String studentId = sc.nextLine().trim();

        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                System.out.print("修改班级 (当前: " + student.getStudentClass() + "): ");
                student.setStudentClass(sc.nextLine().trim());

                System.out.print("修改姓名 (当前: " + student.getName() + "): ");
                student.setName(sc.nextLine().trim());

                System.out.print("修改性别 (当前: " + student.getGender() + "): ");
                student.setGender(sc.nextLine().trim());

                System.out.print("修改年龄 (当前: " + student.getAge() + "): ");
                student.setAge(sc.nextInt());

                saveStudents();
                System.out.println("学生信息修改成功！");
                return;
            }
        }
        System.out.println("未找到该学号的学生！");
    }

    private static void findStudent() {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入要查找的姓名或学号: ");
        String input = sc.nextLine().trim();

        students.stream()
                .filter(s -> s.getName().equals(input) || s.getStudentId().equals(input))
                .forEach(System.out::println);
    }

    private static void filterStudents() {
        Scanner sc = new Scanner(System.in);
        System.out.print("筛选条件 (1: 班级, 2: 性别): ");
        int option = sc.nextInt();
        sc.nextLine();

        if (option == 1) {
            System.out.print("请输入班级: ");
            String studentClass = sc.nextLine().trim();
            students.stream()
                    .filter(s -> s.getStudentClass().equals(studentClass))
                    .forEach(System.out::println);
        } else if (option == 2) {
            System.out.print("请输入性别 (男/女): ");
            String gender = sc.nextLine().trim();
            students.stream()
                    .filter(s -> s.getGender().equals(gender))
                    .forEach(System.out::println);
        } else {
            System.out.println("无效筛选条件！");
        }
    }

    private static void sortStudents() {
        Scanner sc = new Scanner(System.in);
        System.out.print("排序条件 (1: 班级, 2: 学号, 3: 姓名, 4: 性别): ");
        int option = sc.nextInt();

        students.sort(switch (option) {
            case 1 -> Comparator.comparing(Student::getStudentClass);
            case 2 -> Comparator.comparing(Student::getStudentId);
            case 3 -> Comparator.comparing(Student::getName);
            case 4 -> Comparator.comparing(Student::getGender);
            default -> throw new IllegalArgumentException("无效排序条件！");
        });
        students.forEach(System.out::println);
        saveStudents();
    }

    private static void binarySearchByName() {
        students.sort(Comparator.comparing(Student::getName));
        Scanner sc = new Scanner(System.in);
        System.out.print("输入姓名: ");
        String name = sc.nextLine();

        int index = Collections.binarySearch(students, new Student("", "", name, "", 0),
                Comparator.comparing(Student::getName));
        if (index >= 0) {
            System.out.println(students.get(index));
        } else {
            System.out.println("未找到该学生！");
        }
    }

    private static void binarySearchById() {
        students.sort(Comparator.comparing(Student::getStudentId));
        Scanner sc = new Scanner(System.in);
        System.out.print("输入学号: ");
        String id = sc.nextLine();

        int index = Collections.binarySearch(students, new Student("", id, "", "", 0),
                Comparator.comparing(Student::getStudentId));
        if (index >= 0) {
            System.out.println(students.get(index));
        } else {
            System.out.println("未找到该学生！");
        }
    }
}
