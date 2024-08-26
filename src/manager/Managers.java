package manager;


public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(); // Возвращаем стандартный менеджер задач
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager(); // Возвращаем стандартный менеджер истории просмотров
    }
}
