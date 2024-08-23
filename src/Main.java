

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");


                TaskManager manager = new TaskManager();

                /* Тесты согластно ТЗ
                Task task1 = new Task("Переезд", "Собрать коробки");
                manager.addTask(task1);
                Task task2 = new Task("ДР Друга", "Купить подарок");
                manager.addTask(task2);

                Epic epic1 = new Epic("Спорт", "Тренировка");
                manager.addEpic(epic1);

                Subtask subtask1 = new Subtask("Пробежка", "5 км", epic1.getId());
                manager.addSubtask(subtask1);
                Subtask subtask2 = new Subtask("Подтягивания ", "4подхода", epic1.getId());
                manager.addSubtask(subtask2);



                System.out.println("Задачи: " + manager.getAllTasks());
                System.out.println("Эпики: " + manager.getAllEpics());
                System.out.println("Подзадачи: " + manager.getAllSubtasks());


                subtask1.setStatus(Status.DONE);
                manager.updateSubtask(subtask1);
                System.out.println("Обнавления Эпика: " + manager.getEpic(epic1.getId()));
                manager.removeSubtask(subtask1.getId());
                System.out.println("Задачи: " + manager.getAllTasks());
                System.out.println("Эпики: " + manager.getAllEpics());
                System.out.println("Подзадачи: " + manager.getAllSubtasks());
                manager.removeEpic(epic1.getId());
                System.out.println("Задачи: " + manager.getAllTasks());
                System.out.println("Эпики: " + manager.getAllEpics());
                System.out.println("Подзадачи: " + manager.getAllSubtasks());
                manager.removeTask(task1.getId());
                System.out.println("Задачи: " + manager.getAllTasks());
                System.out.println("Эпики: " + manager.getAllEpics());
                System.out.println("Подзадачи: " + manager.getAllSubtasks());*/
    }
}
