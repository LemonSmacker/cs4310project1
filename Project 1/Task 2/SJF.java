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

public class SJF {
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
    simulateSJF(processes);
  }

  public static void simulateSJF(ArrayList<Process> processes) {
    int currentTime = 0;
    int totalWaitingTime = 0;
    int totalTurnaroundTime = 0;
    int totalResponseTime = 0;
    int totalProcesses = processes.size();
    int completedProcesses = 0;
    while (completedProcesses < totalProcesses) {
      Process shortestJob = null;
      for (Process process : processes) {
        if (!process.completed && process.arrivalTime <= currentTime) {
          if (shortestJob == null || process.burstTime < shortestJob.burstTime) {
            shortestJob = process;
          }
        }
      }
      if (shortestJob == null) {
        currentTime++;
        continue;
      }
      shortestJob.waitingTime = currentTime - shortestJob.arrivalTime;
      totalWaitingTime += shortestJob.waitingTime;
      totalTurnaroundTime += shortestJob.waitingTime + shortestJob.burstTime;
      totalResponseTime += currentTime - shortestJob.arrivalTime;
      shortestJob.completionTime = currentTime + shortestJob.burstTime;
      shortestJob.completed = true;
      completedProcesses++;
      currentTime = shortestJob.completionTime;
      System.out.println("Process " + shortestJob.id + " completed at time " + currentTime);
    }
    double avgWaitingTime = (double) totalWaitingTime / totalProcesses;
    double avgTurnaroundTime = (double) totalTurnaroundTime / totalProcesses;
    double avgResponseTime = (double) totalResponseTime / totalProcesses;
    double cpuUtilization = (double) (currentTime - processes.get(0).arrivalTime) / currentTime * 100;
    System.out.println("Average Waiting Time: " + avgWaitingTime);
    System.out.println("Average Response Time: " + avgResponseTime);
    System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    System.out.println("CPU Utilization: " + cpuUtilization + "%");
  }
}