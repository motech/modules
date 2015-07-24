package org.motechproject.http.agent.listener;

import org.apache.http.HttpException;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

/**
 * This class implements callable to retry failed executions of external calls
 * through rest template
 *
 * @author anuranjan
 *
 */
public class RetriableTask<T> implements Callable<T> {

    private Callable<T> task;
    public static final int DEFAULT_NUMBER_OF_RETRIES = 1;
    public static final long DEFAULT_WAIT_TIME = 0;

    private int numberOfTriesLeft; // number left
    private long timeToWait; // wait interval

    public RetriableTask(Callable<T> task) {
        this(DEFAULT_NUMBER_OF_RETRIES, DEFAULT_WAIT_TIME, task);
    }

    public RetriableTask(int numberOfTriesLeft, long timeToWait, Callable<T> task) {
        this.numberOfTriesLeft = numberOfTriesLeft;
        this.timeToWait = timeToWait;
        this.task = task;
    }

    @Override
    public T call() throws HttpException, InterruptedException {
        T t;
        while (numberOfTriesLeft > 0) {
            try {
                t = task.call();
            } catch (InterruptedException | CancellationException e) {
                throw e;
            } catch (Exception e) {
                numberOfTriesLeft--;
                if (numberOfTriesLeft > 0) {
                    Thread.sleep(timeToWait);
                    continue;
                } else {
                    throw (e instanceof HttpException) ?
                            (HttpException) e : new HttpException("HTTP error when executing task", e);
                }
            }
            return t;
        }
        return null;
    }
}
