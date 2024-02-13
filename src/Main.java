import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();

        System.out.println("Вас приветствует трекер задач!");

        while (true) {
            printMenu();
            String itemNumber = scanner.nextLine();

            switch (itemNumber) {
                case "1":
                    printMenuForItem1(scanner, taskManager);
                    break;
                case "2":
                    printMenuForItem2(scanner, taskManager);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Необходимо ввести номер пункта от 1 до 3, а у вас: " + itemNumber);
            }

        }
    }

    static void printMenu() {
        System.out.println("Введите номер пункта, который хотите выполнить:");
        System.out.println("1 - Записать задачу");
        System.out.println("2 - Управлять задачами");
        System.out.println("3 - Выход из приложения");
    }

    static void printTypeOfTask() {
        System.out.println("Введите номер типа задачи:");
        System.out.println("1 - Простая задача");
        System.out.println("2 - Эпик");
        System.out.println("3 - Подзадача");
        System.out.println("4 - Выйти в предыдущее меню");
    }

    static void printMenuForItem2(Scanner scanner, TaskManager taskManager) {
        while (true) {
            System.out.println("Введите номер пункта для выбора действия с задачами:");
            System.out.println("1 - Вывести список всех задач");
            System.out.println("2 - Вывести список подзадач эпика");
            System.out.println("3 - Удалить все задачи");
            System.out.println("4 - Удалить задачу");
            System.out.println("5 - Обновить задачу");
            System.out.println("6 - Изменить статус задачи");
            System.out.println("7 - Выйти в предыдущее меню");

            String subItemFor2 = scanner.nextLine();

            switch (subItemFor2) {
                case "1":
                    if (taskManager.epics.isEmpty() && taskManager.tasks.isEmpty() && taskManager.subtasks.isEmpty()) {
                        System.out.println("Ваш трекер задач пуст.");
                    } else {
                        taskManager.printTasks();
                        for (Epic epic : taskManager.epics.values()) {
                            taskManager.printEpicAndSubtasks(epic.getIdOfTask());
                        }
                    }
                    break;
                case "2":
                    if (taskManager.epics.isEmpty()) {
                        System.out.println("У вас нет ни одного эпика.");
                    } else {
                        while (true) {
                            System.out.println("Укажите номер эпика из предложенного списка уже созданных, " +
                                    "к которому относится Ваша подзадача:");
                            taskManager.getListOfEpics();
                            String epicId = scanner.nextLine();

                            int findEpicId = taskManager.findEpic(epicId);

                            if (findEpicId == 0) {
                                System.out.println("Некорректно введено название эпика.");
                            } else {
                                taskManager.printEpicAndSubtasks(findEpicId);
                                break;
                            }
                        }
                    }
                    break;
                case "3":
                    taskManager.removeAllTasks();
                    System.out.println("Список задач очищен!");
                    break;
                case "4":
                    label:
                    while (true) {
                        printTypeOfTask();
                        String subItemForRemove = scanner.nextLine();

                        switch (subItemForRemove) {
                            case "1":
                                while (true) {
                                    if (taskManager.tasks.isEmpty()) {
                                        System.out.println("Ваш список задач пуст.");
                                        break;
                                    } else {
                                        System.out.println("Укажите номер задачи из предложенного списка " +
                                                "уже созданных, которую желаете удалить:");
                                        taskManager.getListOfTasks();

                                        String idOfTask = scanner.nextLine();

                                        if (taskManager.removeTask(idOfTask)) {
                                            System.out.println("Задача успешно удалена!");
                                            break;
                                        } else {
                                            System.out.println("Неверно указан номер задачи.");
                                        }
                                    }
                                }
                                break;
                            case "2":
                                while (true) {
                                    if (taskManager.epics.isEmpty()) {
                                        System.out.println("Ваш список эпиков пуст.");
                                        break;
                                    } else {
                                        System.out.println("Укажите номер эпика из предложенного списка " +
                                                "уже созданных, который желаете удалить:");
                                        taskManager.getListOfEpics();

                                        String idOfEpic = scanner.nextLine();

                                        if (taskManager.removeEpic(idOfEpic)) {
                                            System.out.println("Эпик успешно удалён!");
                                            break;
                                        } else {
                                            System.out.println("Неверно указан номер эпика.");
                                        }
                                    }
                                }
                                break;
                            case "3":
                                while (true) {
                                    if (taskManager.subtasks.isEmpty()) {
                                        System.out.println("Ваш список подзадач пуст.");
                                        break;
                                    } else {
                                        System.out.println("Укажите номер подзадачи из предложенного списка " +
                                                "уже созданных, которую желаете удалить:");
                                        taskManager.getListOfEpicsAndSubtasks();

                                        String idOfSubtask = scanner.nextLine();

                                        if (taskManager.removeSubtask(idOfSubtask)) {
                                            System.out.println("Подзадача успешно удалена!");
                                            break;
                                        } else {
                                            System.out.println("Неверно указан номер подзадачи.");
                                        }
                                    }
                                }
                                break;
                            case "4":
                                break label;
                            default:
                                System.out.println("Необходимо ввести номер пункта от 1 до 4, а у вас: " +
                                        subItemForRemove);
                        }
                    }
                    break;
                case "5":
                    label:
                    while (true) {
                        printTypeOfTask();
                        String subItemForRUpdate = scanner.nextLine();

                        switch (subItemForRUpdate) {
                            case "1":
                                while (true) {
                                    if (taskManager.tasks.isEmpty()) {
                                        System.out.println("Ваш список задач пуст.");
                                        break;
                                    } else {
                                        System.out.println("Укажите номер задачи из предложенного списка " +
                                                "уже созданных, которую желаете обновить:");
                                        taskManager.getListOfTasks();

                                        String idOfTask = scanner.nextLine();

                                        System.out.println("Напишите новое название задачи:");
                                        String newNameOfTask = scanner.nextLine();

                                        System.out.println("Создайте описание Вашей новой задачи:");
                                        String newDescriptionOfTask = scanner.nextLine();

                                        Status newStatusOfTask = saveStatusOfTask(scanner);

                                        Task task = new Task(newNameOfTask, newDescriptionOfTask, newStatusOfTask);

                                        if (taskManager.updateTask(idOfTask, task)) {
                                            System.out.println("Задача успешно обновлена!");
                                            break;
                                        } else {
                                            System.out.println("Неверно указан номер задачи.");
                                        }
                                    }
                                }
                                break;
                            case "2":
                                while (true) {
                                    if (taskManager.epics.isEmpty()) {
                                        System.out.println("Ваш список эпиков пуст.");
                                        break;
                                    } else {
                                        System.out.println("Укажите номер эпика из предложенного списка " +
                                                "уже созданных, который желаете обновить:");
                                        taskManager.getListOfEpics();

                                        String idOfEpic = scanner.nextLine();

                                        System.out.println("Напишите новое название эпика:");
                                        String newNameOfEpic = scanner.nextLine();

                                        System.out.println("Создайте описание Вашего нового эпика:");
                                        String newDescriptionOfEpic = scanner.nextLine();

                                        Epic epic = new Epic(newNameOfEpic, newDescriptionOfEpic);

                                        if (taskManager.updateEpic(idOfEpic, epic)) {
                                            System.out.println("Эпик успешно обновлён!");
                                            break;
                                        } else {
                                            System.out.println("Неверно указан номер эпика.");
                                        }
                                    }
                                }
                                break;
                            case "3":
                                while (true) {
                                    if (taskManager.subtasks.isEmpty()) {
                                        System.out.println("Ваш список подзадач пуст.");
                                        break;
                                    } else {
                                        System.out.println("Укажите номер подзадачи из предложенного" +
                                                " списка уже созданных, которую желаете обновить:");
                                        taskManager.getListOfEpicsAndSubtasks();

                                        String idOfSubtask = scanner.nextLine();

                                        System.out.println("Напишите новое название подзадачи:");
                                        String newNameOfSubtask = scanner.nextLine();

                                        System.out.println("Создайте описание Вашей новой подзадачи:");
                                        String newDescriptionOfSubtask = scanner.nextLine();

                                        Status newStatusOfSubtask = saveStatusOfTask(scanner);

                                        Subtask subtask = new Subtask(newNameOfSubtask, newDescriptionOfSubtask,
                                                newStatusOfSubtask);

                                        if (taskManager.updateSubtask(idOfSubtask, subtask)) {
                                            System.out.println("Подзадача успешно обновлена!");
                                            break;
                                        } else {
                                            System.out.println("Неверно указан номер старой подзадачи.");
                                        }
                                    }
                                }
                                break;
                            case "4":
                                break label;
                            default:
                                System.out.println("Необходимо ввести номер пункта от 1 до 4, а у вас: " +
                                        subItemForRUpdate);
                        }
                    }
                    break;
                case "6":
                    label:
                    while (true) {
                        System.out.println("Введите номер типа задачи:");
                        System.out.println("1 - Простая задача");
                        System.out.println("2 - Подзадача");
                        System.out.println("3 - Выйти в предыдущее меню");

                        String subItemForRUpdateStatus = scanner.nextLine();

                        switch (subItemForRUpdateStatus) {
                            case "1":
                                while (true) {
                                    if (taskManager.tasks.isEmpty()) {
                                        System.out.println("Ваш список задач пуст.");
                                        break;
                                    } else {
                                        System.out.println("Укажите номер задачи из предложенного списка " +
                                                "уже созданных, у которой желаете обновить статус:");
                                        taskManager.getListOfTasks();

                                        String idOfTask = scanner.nextLine();

                                        Status newStatusOfTask = saveStatusOfTask(scanner);

                                        if (taskManager.updateTaskStatus(idOfTask, newStatusOfTask)) {
                                            System.out.println("Статус задачи успешно обновлён!");
                                            break;
                                        } else {
                                            System.out.println("Неверно указан номер задачи.");
                                        }
                                    }
                                }
                                break;
                            case "2":
                                while (true) {
                                    if (taskManager.subtasks.isEmpty()) {
                                        System.out.println("Ваш список подзадач пуст.");
                                        break;
                                    } else {
                                        System.out.println("Укажите номер подзадачи из предложенного" +
                                                " списка уже созданных, у которой желаете обновить статус:");
                                        taskManager.getListOfEpicsAndSubtasks();

                                        String idOfSubtask = scanner.nextLine();

                                        Status newStatusOfSubtask = saveStatusOfTask(scanner);

                                        if (taskManager.updateSubtaskStatus(idOfSubtask, newStatusOfSubtask)) {
                                            System.out.println("Статус подзадачи успешно обновлён!");
                                            break;
                                        } else {
                                            System.out.println("Неверно указан номер подзадачи.");
                                        }
                                    }
                                }
                                break;
                            case "3":
                                break label;
                            default:
                                System.out.println("Необходимо ввести номер пункта от 1 до 4, а у вас: " +
                                        subItemForRUpdateStatus);
                        }
                    }
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Необходимо ввести номер пункта от 1 до 7, а у вас: " + subItemFor2);
            }
        }
    }

    static void printMenuForItem1(Scanner scanner, TaskManager taskManager) {
        while (true) {
            printTypeOfTask();
            String subItemFor1 = scanner.nextLine();

            switch (subItemFor1) {
                case "1":
                    System.out.println("Напишите название задачи:");
                    String nameOfTask = scanner.nextLine();

                    System.out.println("Создайте описание Вашей задачи:");
                    String descriptionOfTask = scanner.nextLine();

                    Status statusNumberOfTask = saveStatusOfTask(scanner);

                    Task task = new Task(nameOfTask, descriptionOfTask, statusNumberOfTask);
                    taskManager.saveTask(task);
                    System.out.println("Задача успешно добавлена!");
                    break;
                case "2":
                    System.out.println("Напишите название эпика:");
                    String nameOfEpic = scanner.nextLine();

                    System.out.println("Создайте описание Вашего эпика:");
                    String descriptionOfEpic = scanner.nextLine();

                    Epic epic = new Epic(nameOfEpic, descriptionOfEpic);
                    taskManager.saveEpic(epic);
                    System.out.println("Эпик успешно добавлен!");
                    break;
                case "3":
                    System.out.println("Напишите название подзадачи:");
                    String nameOfSubtask = scanner.nextLine();

                    System.out.println("Создайте описание Вашей подзадачи:");
                    String descriptionOfSubtask = scanner.nextLine();

                    Status statusOfSubtask = saveStatusOfTask(scanner);

                    while (true) {
                        if (taskManager.epics.isEmpty()) {
                            System.out.println("Ваш список эпиков пуст.");
                            break;
                        } else {
                            System.out.println("Укажите номер эпика из предложенного списка уже созданных, " +
                                    "к которому относится Ваша подзадача:");
                            taskManager.getListOfEpics();
                            String epicId = scanner.nextLine();
                            Subtask subtask = new Subtask(nameOfSubtask, descriptionOfSubtask, statusOfSubtask);

                            if (taskManager.findEpic(epicId) == 0) {
                                System.out.println("Некорректно введено название эпика.");
                            } else {
                                taskManager.saveSubtask(subtask, epicId);
                                System.out.println("Подзадача успешно добавлена!");
                                break;
                            }
                        }
                    }
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Необходимо ввести номер пункта от 1 до 4, а у вас: " + subItemFor1);
            }
        }
    }

    static Status saveStatusOfTask(Scanner scanner) {
        Status statusNumberOfTask;

        label:
        while (true) {
            System.out.println("Укажите номер статуса:");
            System.out.println("1 - Новая");
            System.out.println("2 - В процессе решения");
            System.out.println("3 - Выполнена");

            String newStatusOfTask = scanner.nextLine();

            switch (newStatusOfTask) {
                case "1":
                    statusNumberOfTask = Status.NEW;
                    break label;
                case "2":
                    statusNumberOfTask = Status.IN_PROGRESS;
                    break label;
                case "3":
                    statusNumberOfTask = Status.DONE;
                    break label;
                default:
                    System.out.println("Необходимо ввести номер статуса от 1 до 3, а у вас: " +
                            newStatusOfTask);
            }
        }
        return statusNumberOfTask;
    }
}
