import java.util.*;
import java.io.*;

import static java.lang.Math.min;

public class DinicAlgo
{
	static final long INF= Long.MAX_VALUE/2;
	
	private static class Edge
	{
		
		public int from,to;
		public Edge residual;
		public long flow;
		public final long capacity;
		
		public Edge(int from,int to,long capacity)
		{
			this.from=from;
			this.to=to;
			this.capacity=capacity;
		}
		
		public boolean isResidual()
		{
			return capacity==0;
		}
		
		public long remainingCapacity()
		{
			return capacity-flow;
		}
		
		public void augment(long bottleneck)
		{
			flow+=bottleneck;
			residual.flow-=bottleneck;
		}
		
		
	}


	private static class DinicSolver
	{		
		final int n,s,t;			// s=source node,t=sink node
		
		private int[] level;
		
		private boolean solved;
		
		private int maxFlow;
		
		private List<Edge>[] graph;
		
		public DinicSolver(int n,int s,int t)
		{
			this.n=n;
			this.s=s;
			this.t=t;
			
			level = new int[n];
			
			initializeGraph();
		}
		
		private void initializeGraph()
		{
			graph = new List[n];
			
			for(int i=0;i<n;i++)
			{
				graph[i] = new ArrayList<Edge>();
			}
		}
		
		public void addEdge(int from,int to,int capacity)
		{
			if(capacity<=0) throw new IllegalArgumentException("Original Edge Capacity <=0 ");
			
			Edge original = new Edge(from,to,capacity);
			Edge opposite = new Edge (to,from,0);
			
			original.residual=opposite;
			opposite.residual=original;
			
			graph[from].add(original);
			graph[to].add(opposite);
		}
		
		public List<Edge>[] debug()   //  For debugging
		{
			calc();
			
			return graph;
		}
		
		public int getMaxFlow()
		{
			calc();
			
			return maxFlow; 
		}
		
		private void calc()
		{
			if(solved) return;
			
			solved=true;
			
			solve();
		}
		
		public void solve()
		{
			int[] next = new int[n];   // To not to direct to dead ends multiple times
			
			while(bfs())
			{
				Arrays.fill(next,0);
				
				while(true)
				{
					long tp = dfs(s,next,INF);   // bottleneck value
					
					if(tp==0)
						break;
					
					maxFlow+=tp;
				}
			}
		}
		
		private boolean bfs()
		{
			Arrays.fill(level,-1);
			
			Deque<Integer> q=new ArrayDeque<>(n);
			
			q.offer(s);
			
			level[s]=0;
			
			while(!q.isEmpty())
			{
				int node=q.poll();
				
				for(Edge ed:graph[node])
				{
					long remCap=ed.remainingCapacity();
					
					if(remCap>0 && level[ed.to]==-1)
					{
						level[ed.to]=level[node]+1;
						q.offer(ed.to);
					}
				}
			}
			
			return level[t]!=-1;	// Sink reachable or not
		}
		
		private long dfs(int node,int[] next,long flow)
		{
			if(node==t)
			{
				return flow;
			}
			
			final int sz=graph[node].size();
			
			for(;next[node]<sz;next[node]++)
			{
				Edge ed=graph[node].get(next[node]);
				
				long remCap=ed.remainingCapacity();
				
				if(remCap>0 && level[ed.to]==level[node]+1)
				{
					long bottleneck = dfs(ed.to,next,min(flow,remCap));
					
					if(bottleneck>0)
					{
						ed.augment(bottleneck);
						
						return bottleneck;
					}
				}
			}
			
			return 0;
		}
		
		
	}

	public static void main(String[] args) throws IOException
	{
		Scanner in = new Scanner(System.in);
		
		int sz1,sz2,sz3;
		
		sz1=in.nextInt();
		sz2=in.nextInt();
		sz3=in.nextInt();
		
		int[] entrances = new int[sz1];
		int[] exits = new int[sz2];
		
		int[][] path = new int[sz3][sz3];
		
		for(int i=0;i<sz1;i++)
		{
			entrances[i]=in.nextInt();
		}
		for(int i=0;i<sz2;i++)
		{
			exits[i]=in.nextInt();
		}
		
		for(int i=0;i<sz3;i++)
		{
			for(int j=0;j<sz3;j++)
			{
				path[i][j]=in.nextInt();
			}
		}
		
		int n,s,t;
		
		n=sz3+2;   // 2 for SuperSource and SuperSink
		s=path.length;
		t=sz3+1;
		
	    DinicSolver solver = new DinicSolver(n, s, t);
	    
	    for(int i=0;i<sz3;i++)
		{
			for(int j=0;j<sz3;j++)
			{
				if(path[i][j]>0)
				{
					solver.addEdge(i,j,path[i][j]);
				}
			}
		}
	    
	    //SuperEdges
	    for(int i:entrances)
	    {
	    	solver.addEdge(s,i,Integer.MAX_VALUE);
	    }
	    
	    for(int i:exits)
	    {
	    	solver.addEdge(i,t,Integer.MAX_VALUE);
	    }
	   /*
	    2 2 6
	    0 1
	    4 5
	    0 0 4 6 0 0
	    0 0 5 2 0 0
	    0 0 0 0 4 4
	    0 0 0 0 6 6
	    0 0 0 0 0 0
	    0 0 0 0 0 0
	    */
	    System.out.printf("Maximum flow: %d\n", solver.getMaxFlow());
	}

}