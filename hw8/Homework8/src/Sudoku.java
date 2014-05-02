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
    public ArrayList<Integer> possibleValues;

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

    public void print()
    {
      System.out.print("(" + row + "," + column + ") : ");
      for (Integer i : possibleValues)
      {
        System.out.print(i + ", ");
      }
      System.out.println();
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

    public void pencilMarks()
    {
      int row, column, grid;
      ArrayList<Entry> finishedEntries = new ArrayList<Entry>();

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

    public void gridCellRule()
    {
      int row, column, grid;
      ArrayList<Entry> finishedEntries = new ArrayList<Entry>();

      for (Entry entry : unsetEntries)
      {
        row = entry.row;
        column = entry.column;
        ArrayList<Integer> uniqueValues = new ArrayList<Integer>(entry.possibleValues);
        ArrayList<Integer> nonUnique = new ArrayList<Integer>();
        grid = entry.grid;

        for (Entry e : grids.get(grid))
        {
          if (!entry.equals(e))
          {
            for (Integer i : uniqueValues)
            {
              if (e.possibleValues.contains((Integer) i))
              {
                nonUnique.add(i);
              }
            }
          }
        }

        for (Integer i : nonUnique)
        {
          uniqueValues.remove((Integer) i);
        }

        if (uniqueValues.size() == 1)
        {
          rows.get(row).set(rows.get(row).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          columns.get(column).set(columns.get(column).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          grids.get(grid).set(grids.get(grid).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          finishedEntries.add(entry);
        }
      }

      for (Entry entry : finishedEntries)
      {
        unsetEntries.remove(entry);
      }
    }
    
    public void rowCellRule()
    {
      int row, column, grid;
      ArrayList<Entry> finishedEntries = new ArrayList<Entry>();

      for (Entry entry : unsetEntries)
      {
        row = entry.row;
        column = entry.column;
        grid = entry.grid;
        ArrayList<Integer> uniqueValues = new ArrayList<Integer>(entry.possibleValues);
        ArrayList<Integer> nonUnique = new ArrayList<Integer>();

        for (Entry e : rows.get(row))
        {
          if (!entry.equals(e))
          {
            for (Integer i : uniqueValues)
            {
              if (e.possibleValues.contains((Integer) i))
              {
                nonUnique.add(i);
              }
            }
          }
        }

        for (Integer i : nonUnique)
        {
          uniqueValues.remove((Integer) i);
        }

        if (uniqueValues.size() == 1)
        {
          rows.get(row).set(rows.get(row).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          columns.get(column).set(columns.get(column).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          grids.get(grid).set(grids.get(grid).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          finishedEntries.add(entry);
        }
      }

      for (Entry entry : finishedEntries)
      {
        unsetEntries.remove(entry);
      }
    }
    
    public void columnCellRule()
    {
      int row, column, grid;
      ArrayList<Entry> finishedEntries = new ArrayList<Entry>();

      for (Entry entry : unsetEntries)
      {
        row = entry.row;
        column = entry.column;
        ArrayList<Integer> uniqueValues = new ArrayList<Integer>(entry.possibleValues);
        ArrayList<Integer> nonUnique = new ArrayList<Integer>();
        grid = entry.grid;

        for (Entry e : columns.get(column))
        {
          if (!entry.equals(e))
          {
            for (Integer i : uniqueValues)
            {
              if (e.possibleValues.contains((Integer) i))
              {
                nonUnique.add(i);
              }
            }
          }
        }

        for (Integer i : nonUnique)
        {
          uniqueValues.remove((Integer) i);
        }

        if (uniqueValues.size() == 1)
        {
          rows.get(row).set(rows.get(row).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          columns.get(column).set(columns.get(column).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          grids.get(grid).set(grids.get(grid).indexOf(entry),
              new Entry(entry.row, entry.column, uniqueValues.get(0)));
          finishedEntries.add(entry);
        }
      }

      for (Entry entry : finishedEntries)
      {
        unsetEntries.remove(entry);
      }
    }

    public void tupleRule()
    {
      int row, column, grid;
      ArrayList<Entry> finishedEntries = new ArrayList<Entry>();
      ArrayList<Entry> tupleEntries = new ArrayList<Entry>();
      Hashtable<Integer, ArrayList<Entry>> tupleCandidates;

      for (grid = 0; grid < grids.size(); grid++)
      {
        tupleCandidates = new Hashtable<Integer, ArrayList<Entry>>();

        // Check each cell in the grid to find the digits that are present in
        // exactly 2 cells
        for (int i = 1; i <= 9; i++)
        {
          ArrayList<Entry> list = new ArrayList<Entry>();
          for (Entry entry : grids.get(grid))
          {
            if (entry.possibleValues.contains(i))
            {
              list.add(entry);
            }
          }

          if (list.size() == 2)
          {
            tupleCandidates.put(i, list);
          }
        }

        // Find the ywo tuple lists that are the same those two entries 
        // can have the rest of their possible values remove
        for (Integer i : tupleCandidates.keySet())
        {
          for (Integer i2 : tupleCandidates.keySet())
          {
            if (i != i2)
            {
              // There will always be two entries in each list
              if (tupleCandidates.get(i).contains(tupleCandidates.get(i2).get(0))
                  && tupleCandidates.get(i).contains(tupleCandidates.get(i2).get(1)))
              {
                Entry e1 = tupleCandidates.get(i).get(0);
                Entry e2 = tupleCandidates.get(i).get(1);
                ArrayList<Integer> tupleValues = new ArrayList<Integer>();
                tupleValues.add(i);
                tupleValues.add(i2);
                e1.possibleValues = tupleValues;
                e2.possibleValues = tupleValues;

                //e1.print();
                //e2.print();
                //System.out.println();

                rows.get(e1.row).set(rows.get(e1.row).indexOf(e1), e1);
                columns.get(e1.column).set(columns.get(e1.column).indexOf(e1), e1);
                grids.get(e1.grid).set(grids.get(e1.grid).indexOf(e1), e1);

                rows.get(e2.row).set(rows.get(e2.row).indexOf(e2), e2);
                columns.get(e2.column).set(columns.get(e2.column).indexOf(e2), e2);
                grids.get(e2.grid).set(grids.get(e2.grid).indexOf(e2), e2);
              }
            }
          }
        }
      }

      for (Entry entry : finishedEntries)
      {
        unsetEntries.remove(entry);
      }
    }

    public void uniqueDigitRule()
    {
      int row, column, grid;
      ArrayList<Entry> finishedEntries = new ArrayList<Entry>();

//      for (grid = 0; grid < grids.size(); grid++)
//      {
//        for (int i = 1; i <= 9; i++)
//        {
//          ArrayList<Entry> list = new ArrayList<Entry>();
//          for (Entry entry : grids.get(grid))
//          {
//            if (!entry.isSet() && entry.possibleValues.contains(i))
//            {
//              list.add(entry);
//            }
//          }
//
//          if (list.size() == 1)
//          {
//            Entry entry = list.get(0);
//            rows.get(entry.row).set(rows.get(entry.row).indexOf(entry),
//                new Entry(entry.row, entry.column, i));
//            columns.get(entry.column).set(columns.get(entry.column).indexOf(entry),
//                new Entry(entry.row, entry.column, i));
//            grids.get(entry.grid).set(grids.get(entry.grid).indexOf(entry),
//                new Entry(entry.row, entry.column, i));
//            unsetEntries.remove(entry);
//          }
//        }
//      }

//      for (row = 0; row < rows.size(); row++)
//      {
//        for (int i = 1; i <= 9; i++)
//        {
//          ArrayList<Entry> list = new ArrayList<Entry>();
//          System.out.println(i +" : ");
//          for (Entry entry : rows.get(row))
//          {
//            if (!entry.isSet() && entry.possibleValues.contains(i))
//            {
//              entry.print();
//              list.add(entry);
//            }
//          }
//
//          if (list.size() == 1)
//          {
//            Entry entry = list.get(0);
//            Entry e = new Entry(entry.row, entry.column, i);
//            
//            //e.print();
//
//            rows.get(e.row).set(rows.get(e.row).indexOf(entry), e);
//            columns.get(e.column).set(columns.get(e.column).indexOf(entry), e);
//            grids.get(e.grid).set(grids.get(e.grid).indexOf(entry), e);
//            unsetEntries.remove(entry);
//          }
//        }
//      }
      //      
      //      for (column = 0; column < columns.size(); column++)
      //      {
      //        for (int i = 1; i <= 9; i++)
      //        {
      //          ArrayList<Entry> list = new ArrayList<Entry>();
      //          for (Entry entry : columns.get(column))
      //          {
      //            if (!entry.isSet() && entry.possibleValues.contains(i))
      //            {
      //              list.add(entry);
      //            }
      //          }
      //
      //          if (list.size() == 1)
      //          {
      //            Entry entry = list.get(0);
      //            rows.get(entry.row).set(rows.get(entry.row).indexOf(entry),
      //                new Entry(entry.row, entry.column, i));
      //            columns.get(entry.column).set(columns.get(entry.column).indexOf(entry),
      //                new Entry(entry.row, entry.column, i));
      //            grids.get(entry.grid).set(grids.get(entry.grid).indexOf(entry),
      //                new Entry(entry.row, entry.column, i));
      //            unsetEntries.remove(entry);
      //          }
      //        }
      //      }
    }

    public void solve()
    {
      int row;

      //while (unsetEntries.size() > 0)
      for (int i = 0; i < 30; i++)
      {
        pencilMarks();
        gridCellRule();
        rowCellRule();
        columnCellRule();
        tupleRule();
        //uniqueDigitRule();
      }

//      for (row = 0; row < rows.size(); row++)
//      {
//        for (Entry entry : rows.get(row))
//        {
//          entry.print();
//        }
//      }

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
