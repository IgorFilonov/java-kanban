import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");


                TaskManager manager = new TaskManager();

                // Тесты согластно ТЗ
               /* tasks.Task task1 = new tasks.Task("Переезд", "Собрать коробки");
                manager.addTask(task1);
                tasks.Task task2 = new tasks.Task("ДР Друга", "Купить подарок");
                manager.addTask(task2);

                tasks.Epic epic1 = new tasks.Epic("Спорт", "Тренировка");
                manager.addEpic(epic1);

                tasks.Subtask subtask1 = new tasks.Subtask("Пробежка", "5 км", epic1.getId());
                manager.addSubtask(subtask1);
                tasks.Subtask subtask2 = new tasks.Subtask("Подтягивания ", "4подхода", epic1.getId());
                manager.addSubtask(subtask2);



                System.out.println("Задачи: " + manager.getAllTasks());
                System.out.println("Эпики: " + manager.getAllEpics());
                System.out.println("Подзадачи: " + manager.getAllSubtasks());


                subtask1.setStatus(tasks.Status.DONE);
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
