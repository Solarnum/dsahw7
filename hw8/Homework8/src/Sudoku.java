import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Sudoku
{

  private static final int SZ = 9;
  private static int[][] cells;

  public Sudoku(int[][] rCells)
  {
    // TODO Auto-generated constructor stub
  }

  private void printResults()
  {
    System.out.println("    A B C   D E F   G H I");
    System.out.println("   ------- ------- -------");
    for (int j = 0; j < SZ; j++)
      {
        System.out.print(j + 1 + " | ");
        for (int i = 0; i < SZ; i++)
          {
            if(cells[j][i] == 0){
            System.err.print(cells[j][i] + " ");
            }
            else{
              System.out.print(cells[j][i] + " ");
            }
            if ((i + 1) % 3 == 0 && i > 0)
              {
                System.out.print("| ");
              }
          }

        System.out.println();
        if ((j + 1) % 3 == 0)
          {
            System.out.println("   ------- ------- -------");
          }
      }

  }

  private void solve()
  {
    // TODO Auto-generated method stub

  }

  public static void main(String[] args) throws FileNotFoundException
  {
    // TODO Auto-generated method stub
    if (args.length != 1)
      {
        System.out.println("Usage: Sudoku <inputfilename>");
        System.exit(0);
      }

    int i = 0, j;
    cells = new int[SZ][SZ];
    Scanner sc = new Scanner(new File(args[0]));
    while (sc.hasNextLine())
      {
        for (j = 0; j < SZ; j++)
          {
            cells[i][j] = sc.nextInt();
            // System.err.print(""+cells[i][j] + " ");
          }
        // System.out.println();
        i++;
        if (i >= SZ)
          break;
      }
    Sudoku theGame = new Sudoku(cells);
    theGame.solve();
    theGame.printResults();
  }

}
