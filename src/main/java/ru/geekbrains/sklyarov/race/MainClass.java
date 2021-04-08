package ru.geekbrains.sklyarov.race;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {

    // Количество участников
    private static final int CARS_COUNT = 4;

    // Барьер для старта
    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);
    // Семафор для тоннеля
    private final static  Semaphore semaphore = new Semaphore(2);
    // сначала сделал через монитор, потом переделал на CountDownLatch
    private final static CountDownLatch countDownLatch = new CountDownLatch(CARS_COUNT);

    public static void main(String[] args) throws InterruptedException {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(semaphore), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cyclicBarrier, countDownLatch);
        }

        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
//        synchronized (lock) {
//            lock.wait();
//        }
        countDownLatch.await();

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
