import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class Sudoku
{
  private static class Entry
  {
    public int row, column, grid, value;
    public List<Integer> possibleValues;

    public Entry(int row, int column, int value)
    {
      this.value = value;
      this.row = row;
      this.column = column;
      possibleValues = new ArrayList<Integer>();
      setGridNum();

      if (isSet())
      {
        possibleValues.add(value);
      } else
      {
        for (int i = 1; i < 10; i++)
        {
          possibleValues.add(i);
        }
      }
    }

    public boolean isSet()
    {
      return (value != 0);
    }

    private void setGridNum()
    {
      if (row < 3)
      {
        if (column < 3)
        {
          grid = 0;
        } else if (column < 6)
        {
          grid = 1;
        } else
        {
          grid = 2;
        }
      } else if (row < 6)
      {
        if (column < 3)
        {
          grid = 3;
        } else if (column < 6)
        {
          grid = 4;
        } else
        {
          grid = 5;
        }
      } else
      {
        if (column < 3)
        {
          grid = 6;
        } else if (column < 6)
        {
          grid = 7;
        } else
        {
          grid = 8;
        }
      }
    }
  }

  private static class SudokuGame
  {
    private ArrayList<Entry> unsetEntries;
    private Hashtable<Integer, ArrayList<Entry>> rows;
    private Hashtable<Integer, ArrayList<Entry>> columns;
    private Hashtable<Integer, ArrayList<Entry>> grids;
    private int unsetValue = 0;

    public SudokuGame(int[][] cells)
    {
      rows = new Hashtable<Integer, ArrayList<Entry>>();
      columns = new Hashtable<Integer, ArrayList<Entry>>();
      grids = new Hashtable<Integer, ArrayList<Entry>>();
      unsetEntries = new ArrayList<Entry>();

      // Assuming it is a 9x9 matrix... can throw exceptions
      for (int i = 0; i < 9; i++)
      {
        for (int j = 0; j < 9; j++)
        {
          Entry entry = new Entry(i, j, cells[i][j]);
          if (cells[i][j] == unsetValue)
          {
            unsetEntries.add(entry);
          }
          addRowEntry(entry);
          addColumnEntry(entry);
          addGridEntry(entry);
        }
      }
    }

    public void solve()
    {
      int row, column, grid;
      ArrayList<Entry> finishedEntries = new ArrayList<Entry>();

      while (unsetEntries.size() > 0)
      {
        for (Entry entry : unsetEntries)
        {
          row = entry.row;
          column = entry.column;
          grid = entry.grid;

          if (!entry.isSet())
          {
            for (Entry e : rows.get(row))
            {
              entry.possibleValues.remove((Integer) e.value);
            }
            for (Entry e : columns.get(column))
            {
              entry.possibleValues.remove((Integer) e.value);
            }
            for (Entry e : grids.get(grid))
            {
              entry.possibleValues.remove((Integer) e.value);
            }

            if (entry.possibleValues.size() == 1)
            {
              rows.get(row).set(rows.get(row).indexOf(entry),
                  new Entry(entry.row, entry.column, entry.possibleValues.get(0)));
              columns.get(column).set(columns.get(column).indexOf(entry),
                  new Entry(entry.row, entry.column, entry.possibleValues.get(0)));
              grids.get(grid).set(grids.get(grid).indexOf(entry),
                  new Entry(entry.row, entry.column, entry.possibleValues.get(0)));
              finishedEntries.add(entry);
            }
          }
        }

        for (Entry entry : finishedEntries)
        {
          unsetEntries.remove(entry);
        }
      }

      if (!validSums())
      {
        System.out.println("An error occurred during the calculations. The sums are incorrect.");
        System.out.println();
      }
    }

    public void printTable()
    {
      System.out.println("    A B C   D E F   G H I");
      System.out.println("   ------- ------- -------");
      for (int i = 0; i < rows.keySet().size(); i++)
      {
        System.out.print(i + 1 + " | ");
        int count = 1;
        for (Entry j : rows.get(i))
        {
          System.out.print(j.value + " ");
          if ((count) % 3 == 0)
          {
            System.out.print("| ");
          }

          count++;
        }

        System.out.println();
        if ((i + 1) % 3 == 0)
        {
          System.out.println("   ------- ------- -------");
        }
      }

      System.out.println();
    }

    private void addRowEntry(Entry entry)
    {
      if (rows.containsKey(entry.row))
      {
        rows.get(entry.row).add(entry);
      } else
      {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(entry);

        rows.put(entry.row, entries);
      }
    }

    private void addColumnEntry(Entry entry)
    {
      if (columns.containsKey(entry.column))
      {
        columns.get(entry.column).add(entry);
      } else
      {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(entry);

        columns.put(entry.column, entries);
      }
    }

    private void addGridEntry(Entry entry)
    {
      if (grids.containsKey(entry.grid))
      {
        grids.get(entry.grid).add(entry);
      } else
      {
        ArrayList<Entry> entries = new ArrayList<Entry>();
        entries.add(entry);

        grids.put(entry.grid, entries);
      }
    }

    private boolean validSums()
    {
      boolean completed = true;
      int sum;

      for (int i = 0; i < rows.keySet().size(); i++)
      {
        sum = 0;
        for (Entry j : rows.get(i))
        {
          sum += j.value;
        }

        if (sum != 45)
        {
          completed = false;
          break;
        }
      }

      for (int j = 0; j < columns.keySet().size() && completed; j++)
      {
        sum = 0;
        for (Entry i : columns.get(j))
        {
          sum += i.value;
        }

        if (sum != 45)
        {
          completed = false;
          break;
        }
      }

      for (int grid = 0; grid < grids.keySet().size() && completed; grid++)
      {
        sum = 0;
        for (Entry k : grids.get(grid))
        {
          sum += k.value;
        }

        if (sum != 45)
        {
          completed = false;
          break;
        }
      }

      return completed;
    }

  }

  private static final int SZ = 9;
  private static Scanner sc;

  public static void main(String[] args) throws FileNotFoundException
  {
    if (args.length != 1)
    {
      System.out.println("Usage: Sudoku <inputfilename>");
      System.exit(0);
    }

    int i = 0, j;
    int[][] cells = new int[SZ][SZ];
    sc = new Scanner(new File(args[0]));
    while (sc.hasNextLine())
    {
      for (j = 0; j < SZ; j++)
      {
        cells[i][j] = sc.nextInt();
      }
      i++;
      if (i >= SZ)
        break;
    }

    SudokuGame game = new SudokuGame(cells);
    game.solve();
    game.printTable();
  }

}
