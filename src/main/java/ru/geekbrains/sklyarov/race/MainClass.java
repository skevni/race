package ru.geekbrains.sklyarov.race;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
/*  Поясню себе, если вдруг забуду.
    1. Выше создал CyclicBarrier и передал его в каждый объект машины. По сути барьер у нас один, что
        в main, что в экземплярах класса Car. Почему размер барьера = кол-во машин + 1.
        Потому что 1 это главный поток исполнения (main)
        Поток main проходя по коду упирается в первый "барьер 1" и ждет еще 4х машин в "барьере 2"
    2. Для каждого экземпляра Car создается поток и в них машины готовятся к гонке, после вывода "готов к гонке"
        установлены еще 2: "барьере 2" и "барьере 3" - это кстати тот же самый объект,
        поэтому 1 уже ждет в главном классе "барьер 1" и когда все машины доходят до "барьер 2", то сразу оба барьера:
        "барьер 1" и "барьер 2" снимаются
    3. Для того чтобы гарантированно напечаталось сообщение "... Гонка началась", необходимо в классе Car установить
        сразу после "барьер 2" "барьер 3", чтобы машины не успели ехать. Что происходит: главный поток исполнения
        main упирается в "барьер 4", а машины в "поток 3", причем main-поток уже вывел сообщение о начале гонки. Добились
        того, что машины еще не поехали, а сообщение появилось
    4. Машины дошли до "барьера 3" и одновременно снимаются "барьер 3" и "барьер 4". Main-поток упирается в "барьер 5", а
        машины поехали по стадиям(трассам и тоннелям) - метод go. Какой первый поток добрался до синхронизированного
        метода winner, тот и победитель. В этом методе печатается "Поток № .. - WIN". Почему метод winner static? Для того
        чтобы синхронизация была по классу, т.к. каждая машина не по одельности участвует так сказать, а в группе (Класс).
    5. Итак, после выполнения метода winner каждым потоком, потоки машин упираются в "барьер 6" и помним, что их уже
        main-поток ждет в "барьере 5". В сумме main + 4 маштны дают границу барьера, "барьер 6" и "барьер 5" открываются
        метод go в классе Car заканчивается, а из метода main печатается финальная строка об окончании гонки.
    6. Семафор инициализируется в конструкторе Tunnel(не статический, говорят, что если можно избавиться от статити, то
        избавляемся. В конструктор передаем пропускную способность тоннеля. Далее, как только до семафора доходят две
        машины, остальные тормозятся. Первая вышла из тоннеля и из семафора себя убрала (т.е. счетчик вернулся на 1 +),
        захватила следующая машина и поехала в тоннель, остальные ждут освобождения.
 */
public class MainClass {

    // Количество участников
    public static final int CARS_COUNT = 4;

    // Барьер для старта
    // +1 т.к. кроме потоков для машин есть еще главный поток
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT + 1);

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(CARS_COUNT/2), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cyclicBarrier);
        }

        for (Car car : cars) {
            new Thread(car).start();
        }

        cyclicBarrier.await(); // барьер 1
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        cyclicBarrier.await(); // барьер 4
        cyclicBarrier.await(); // барьер 5

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
