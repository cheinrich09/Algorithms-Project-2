package part2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.PriorityQueue;

public class HuffmanCode {
	
	BufferedReader reader;
	PrintStream output;
	
	public class Letter
	{
		private char letter;
		private int frequency;
		public Letter(char l, int v)
		{
			letter = l;
			frequency = v;
		}
		
		public char Key()
		{
			return letter;
		}
		public int Freq()
		{
			return frequency;
		}
		@Override
		public String toString()
		{
			return letter+": "+frequency;
		}
	}
	
	public class Node implements Comparable<Node>
	{
		//private char key;
		//private int frequency;
		//public String key;
		private Letter value;
		private Node left, right;
		
		public Node Left()
		{
			return left;
		}
		public Node Right()
		{
			return right;
		}
		public Node(Letter x)
		{
			//key = k;
			//frequency = 1;
			//l = new Letter(k, 1);
			value = x;
			left = null;
			right = null;
		}
		public Node(Node l, Node r)
		{
			value = new Letter(' ', l.value.Freq()+ r.value.Freq());
			left = l;
			right = r;
			
		}
		// is the node a leaf node?
        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }
        // compare, based on frequency
        public int compareTo(Node that) {
            return this.value.Freq() - that.value.Freq();
        }

	}
	public class LetterList {
		List<Letter> letters;
		String lookup[];
		public Node root;
		public LetterList()
		{
			letters = new ArrayList<Letter>();
			lookup = new String[256];
			//nodes
			
		}
		public char find(String s)
		{
			for (Letter l : letters)
			{
				if (lookup[l.letter] == s)
				{
					return l.letter;
				}
			}
			return '-';
		}
		public void add(char s)
		{
			int index = getIndex(s);
			if (index > -1)
			{
				letters.get(index).frequency++;
			}
			else
			{
				letters.add(new Letter(s, 1));
			}
		}
		public int getIndex(char s)
		{
			for (int i = 0; i < letters.size(); i++)
			{
				if(letters.get(i).Key() == s)
				{
					return i;
				}
			}
			return -1;
		}
		public void buildTree()
		{
			PriorityQueue<Node> nodes = new PriorityQueue<Node>();
			for (Letter x : letters)
			{
				nodes.add(new Node(x));
			}
			while(nodes.size() > 1)
			{
				Node left = nodes.poll();
				Node right = nodes.poll();
				nodes.add(new Node(left, right));
			}
			root = nodes.poll();
		}
	}
	List<String> lines;
	LetterList lList;
	LetterList inList;
	
	public HuffmanCode()
	{
		lines = new ArrayList<String>();
		lList = new LetterList();
		inList = new LetterList();
		//compressed = new ArrayList<byte>();
		//letters = new ArrayList<Letter>();
	}
	
	public void encode(String filepath, String filename)
	{
		
		try
		{
			reader = new BufferedReader(new FileReader((filepath+filename)));
		}
		catch(FileNotFoundException f)
		{
			f.printStackTrace();
			return;
		}
		String line;
		try {
			while((line = reader.readLine()) != null)
			{
				lines.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String l: lines)
		{
			//System.out.println(l);
			//System.out.println(l.charAt(4));
			for (int i = 0; i < l.length(); i++)
			{
				//if(l.charAt(i)!= ' ')
				//{
					lList.add(l.charAt(i));
				//}
			}
		}
		lList.buildTree();
		buildCode(lList.lookup, lList.root, "");
		try {
			//output = new FileOutputStream(new File(filepath+"encoded"+filename));
			output = new PrintStream((filepath+"encoded"+filename));
			//treeOutput = new  PrintStream((filepath+"tree"+filename));
			//output = new ObjectOutputStream(new FileOutputStream(filepath+"tree"+filename));
			//treeOutput = new ObjectOutputStream(new FileOutputStream(filepath+"encoded"+filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BitSet bitSet = new BitSet();
		bitObject o = new bitObject();
		try {
			order(lList.root, bitSet, o);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			saveFreq(lList.root);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (String l: lines)
		{
			try {
				
				message(l);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//output.println();
		}
		output.close();
		
	}
	public void decode(String filepath, String filename)
	{
		StringBuilder code = new StringBuilder();
		
		char key = '-';
		//StringBuilder sb = new StringBuilder();
		try
		{
			reader = new BufferedReader(new FileReader((filepath+filename)));
		}
		catch(FileNotFoundException f)
		{
			f.printStackTrace();
			return;
		}
		String line;
		try {
			while((line = reader.readLine()) != null)
			{
				lines.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String l: lines)
		{
			//System.out.println(l);
			//System.out.println(l.charAt(4));
			if(l.charAt(0) == '(')
			{
				StringBuilder sb  = new StringBuilder();;
				for (int i = 0; i < l.length(); i++)
				{
					//if(l.charAt(i)!= ' ')
					//{
						//lList.add(l.charAt(i));
					//
					if (l.charAt(i) == '[')
					{
						key = l.charAt(i+1);
						i++;
						sb = new StringBuilder();
					}
					else if(l.charAt(i)!='[')
					{
						sb.append(l.charAt(i));
					}
					else if(l.charAt(i) == ']')
					{
						inList.lookup[key] = sb.toString();
						inList.add(key);
						//System.out.println(sb.toString());
					}
				}
			}
			else
			{
				StringBuilder finalize = new StringBuilder();
				
				for (int i = 0; i < l.length(); i++)
				{
					finalize.append(i);
					char temp = inList.find(finalize.toString());
					if(temp !='-')
					{
						finalize  = new StringBuilder();
						code.append(temp);
					}
				}
				
			}
		}
		System.out.println(code.toString());
	}
	
	private void saveFreq(Node node) throws IOException
	{
		if (node.isLeaf())
		{
			char val = node.value.letter;
			String code = lList.lookup[val];
			String unit = "["+val+code+"]";
			output.print(unit);
			return;
		}
		saveFreq(node.left);
		saveFreq(node.right);
	}
	private class bitObject {
		int position;
	}
	
	private void order(Node node, BitSet bitSet, bitObject obj) throws IOException
	{
		if (node.isLeaf())
		{
			bitSet.set(obj.position++, false);
			return;
		}
		bitSet.set(obj.position++, true);           // register branch in bitset
        order(node.left, bitSet, obj); // take the branch.

        bitSet.set(obj.position++, true);               // register branch in bitset
        order(node.right, bitSet, obj); 
	}
	
	private void message(String message) throws IOException {
		//System.out.println(message);
		String temp = translate(message);
		for (int i = 0; i < temp.length(); i++)
		{
			
			
			//output.print(temp.charAt(i));
			if (temp.charAt(i) == '0')
			{
				//output.write(0);
				output.print(0);
				//output.print(c);
			}
			else if (temp.charAt(i) == '1')
			{
				//output.write(0);
				//System.out.println("test");
				output.print(1);
			}
			output.flush();
		}
	}
	
	
	private String translate(String message)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i< message.length(); i++)
		{
			stringBuilder.append(lList.lookup[message.charAt(i)]);
		}
		//System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}
	private void buildCode(String[] st, Node x, String s)
	{
		if(!x.isLeaf())
		{
			buildCode(st, x.Left(), s+'0');
			buildCode(st, x.Right(), s+'1');
		}
		else
		{
			st[x.value.letter] = s;
		}
	}
}
