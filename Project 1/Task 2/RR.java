import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

class Process {
  int id;
  int arrivalTime;
  int burstTime;
  int priority;
  int waitingTime;
  int completionTime;
  int remainingTime;
  boolean completed;
  Process(int id, int arrivalTime, int burstTime, int priority) {
    this.id = id;
    this.arrivalTime = arrivalTime;
    this.burstTime = burstTime;
    this.priority = priority;
    this.waitingTime = 0;
    this.completionTime = 0;
    this.remainingTime = burstTime;
    this.completed = false;
  }
}

public class RR {
  public static void main(String[] args) {
    ArrayList<Process> processes = new ArrayList<>();
    int timeQuantum = 0;
    try {
      File file = new File("input.txt");
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String[] data = scanner.nextLine().split(" ");
        if (data.length == 1) {
          timeQuantum = Integer.parseInt(data[0]);
          break;
        }
        int id = Integer.parseInt(data[0]);
        int arrivalTime = Integer.parseInt(data[1]);
        int burstTime = Integer.parseInt(data[2]);
        int priority = Integer.parseInt(data[3]);
        processes.add(new Process(id, arrivalTime, burstTime, priority));
      }
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    simulateRR(processes, timeQuantum);
  }

  public static void simulateRR(ArrayList<Process> processes, int timeQuantum) {
    int currentTime = 0;
    int totalWaitingTime = 0;
    int totalTurnaroundTime = 0;
    int totalResponseTime = 0;
    int totalProcesses = processes.size();
    while (!processes.isEmpty()) {
      for (int i = 0; i < processes.size(); i++) {
        Process currentProcess = processes.get(i);
        if (!currentProcess.completed && currentProcess.arrivalTime <= currentTime) {
          int remainingBurstTime = currentProcess.burstTime;
          if (remainingBurstTime > timeQuantum) {
            currentTime += timeQuantum;
            currentProcess.burstTime -= timeQuantum;
          } else {
            currentTime += remainingBurstTime;
            currentProcess.burstTime = 0;
            currentProcess.completionTime = currentTime;
            currentProcess.completed = true;
            totalWaitingTime += currentTime - currentProcess.arrivalTime - remainingBurstTime;
            totalTurnaroundTime += currentTime - currentProcess.arrivalTime;
            totalResponseTime += currentTime - currentProcess.arrivalTime - remainingBurstTime;
            System.out.println("Process " + currentProcess.id + " completed at time " + currentTime);
            processes.remove(currentProcess);
            i--;
          }
        }
      }
    }
    double avgWaitingTime = (double) totalWaitingTime / totalProcesses;
    double avgTurnaroundTime = (double) totalTurnaroundTime / totalProcesses;
    double avgResponseTime = (double) totalResponseTime / totalProcesses;
    double cpuUtilization = 100;
    System.out.println("Average Waiting Time: " + avgWaitingTime);
    System.out.println("Average Response Time: " + avgResponseTime);
    System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    System.out.println("CPU Utilization: " + cpuUtilization + "%");
  }
}