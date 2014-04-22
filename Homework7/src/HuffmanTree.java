import java.io.*;
import java.util.PriorityQueue;

class HuffmanNode
    implements Comparable
{

  private char label;
  private int weight;
  private HuffmanNode leftChild;
  private HuffmanNode rightChild;
  private boolean isRoot = false;

  public HuffmanNode(int freq, HuffmanNode leftChild, HuffmanNode rightChild)
  {
    this.weight = freq;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
    isRoot = true;
  }

  public HuffmanNode(char c, int freq)
  {
    this.label = c;
    this.weight = freq;
  }
  public boolean isRoot()
  {
    return isRoot;
  }
  public char getLabel()
  {
    return label;
  }

  public int getWeight()
  {
    return weight;
  }

  public void setLabel(char c)
  {
    this.label = c;
  }

  public void setWeight(int freq)
  {
    this.weight = freq;
  }

  public HuffmanNode getLeftChild()
  {
    return leftChild;
  }

  public HuffmanNode getRightChild()
  {
    return rightChild;
  }

  public void setRightChild(HuffmanNode rightChild)
  {
    this.rightChild = rightChild;
  }

  public void setLeftChild(HuffmanNode leftChild)
  {
    this.leftChild = leftChild;
  }

  @Override
  public int compareTo(Object object)
  {
    return compareTo((HuffmanNode) object);
  }

  public int compareTo(HuffmanNode hn)
  {
    return this.weight - hn.getWeight();
  }

}

public class HuffmanTree
{
  HuffmanNode root;
  PriorityQueue<HuffmanNode> myQueue;

  private void printCodewords()
  {
    //use printCodes helper method
    printCodes(root, "");
  }
  
  private void printCodes(HuffmanNode node, String string)
  {
    if(!node.isRoot())
      {
        //If not a root/parent node print out the label and generated Huffman Code
        System.out.println(node.getLabel() +": "+ string);
      }
    else
      {
        //For left child add "0" to string 
        String lString = string.concat("0");
        printCodes(node.getLeftChild(), lString);
        
        //For right child add "1" to string
        String rString = string.concat("1");
        printCodes(node.getRightChild(), rString);
      }
  }

  private void buildHuffmanTree()
  {
    HuffmanNode pn = null;
    HuffmanNode pn2 = null;

    while (myQueue.size() > 1)
      {
        //Take top two nodes off the PQ
        HuffmanNode hn = myQueue.poll();
        HuffmanNode hn2 = myQueue.poll();
        //Combine weights into new parent/root node and add back to PQ
        myQueue.add(new HuffmanNode(hn.getWeight() + hn2.getWeight(), hn, hn2));
      }
    root = myQueue.poll();

  }

  private void initQueue(String string)
  {
    myQueue = new PriorityQueue<>();
    BufferedReader br;
    try
      {
        FileReader fr = new FileReader(string);
        br = new BufferedReader(fr);
        for (String line; (line = br.readLine()) != null;)
          {
            char fc = line.charAt(0);
            if (line.charAt(1) == ':')
              {
                String freq = line.substring(2, line.length());
                HuffmanNode hn = new HuffmanNode(fc, Integer.parseInt(freq));
                myQueue.add(hn);
              }
          }

      }
    catch (Exception e)
      {
        e.printStackTrace();
      }

//    for (HuffmanNode h : myQueue)
//      {
//        System.out.println(h.getLabel() + ":" + h.getWeight());
//      }

  }

  public static void main(String args[]) throws FileNotFoundException
  {
    if (args.length != 1)
      {
        System.out.println("Usage: HuffmanTree <inputfilename>");
        System.exit(0);
      }
    HuffmanTree tree = new HuffmanTree();
    tree.initQueue(args[0]);
    tree.buildHuffmanTree();
    System.out.println("The Huffman codewords for the alphabet and weights specified in "
                       + args[0] + " is:");
    tree.printCodewords();
  }

}
