package ru.geekbrains.sklyarov.race;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    private final CyclicBarrier cyclicBarrier;
    // Когда я использовал в качестве монитора обертку Boolean без final, идея меня предупреждала, что так лучше не делать
    // поэтому сделал мониторы и переменные
    private static boolean isWinner = false;
    private static boolean isOutLine = false;
    private final static Boolean monWinner = false;
    private final static Boolean monOutLine = false;

    private final CountDownLatch countDownLatch;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cyclicBarrier = cyclicBarrier;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            // ждем всех
            cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        synchronized (monOutLine) {
            if (!isOutLine) {
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
                isOutLine = true;
            }
        }
        for (int i = 0; i < race.getStages().size(); i++) {

            race.getStages().get(i).go(this);

            synchronized (monWinner) {
                if (i == race.getStages().size() - 1) {
                    if (!isWinner) {
                        System.err.println(name + " - WIN ---------------------");
                        isWinner = true;
                    }
                    countDownLatch.countDown();
//                    try {
//                        cyclicBarrier.await();
//                    } catch (InterruptedException | BrokenBarrierException e) {
//                        e.printStackTrace();
//                    }
//                    synchronized (MainClass.lock) {
//                        MainClass.lock.notifyAll();
//                    }
                }
            }
        }
    }
}
