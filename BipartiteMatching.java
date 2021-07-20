import java.util.*; 
import java.io.*;


class Edge{
  private Set<String> nodes;

  public Edge(String node){
    this.nodes = new HashSet<String>();
    this.nodes.add(node);
  }

  public Set<String> getNodes(){
    return this.nodes;
  }
}

class Main{
  public static String BipartiteMatching(String[] strArr) {
    TreeMap<String, Edge> edges = new TreeMap<String, Edge>();
    TreeMap<String, Edge> inverseEdges = new TreeMap<String, Edge>();

    Set<String> x = new HashSet<String>();
    Set<String> y = new HashSet<String>();

    int maxMatching = 0;
    
    for(int i = 0; i<strArr.length; i++){
      String[] pairs = strArr[i].split("->");
      addEdge(edges, inverseEdges, pairs);
      x.add(pairs[0]);
      y.add(pairs[1]);
    }

    TreeMap<String, String> BFSMatches = BFS(edges);

    if (BFSMatches.size()==x.size() || BFSMatches.size()==y.size()){
      maxMatching = BFSMatches.size();
    } else {
      Set<String> unmatchedX = new HashSet<String>();
      Set<String> unmatchedY = y;

      for(String uX:x){
        if (!BFSMatches.keySet().contains(uX)){
          unmatchedX.add(uX);
        }else{
          unmatchedY.remove(BFSMatches.get(uX));
        }
      }

      TreeMap<String, String> DFSMatches = DFS(edges, inverseEdges, BFSMatches, unmatchedX, unmatchedY);
      maxMatching = DFSMatches.size();
    }

    return Integer.toString(maxMatching);
  }

  public static TreeMap<String, String> DFS(TreeMap<String, Edge> edges, TreeMap<String, Edge> inverseEdges, TreeMap<String, String> BFSMatches,
    Set<String> unmatchedX, Set<String> unmatchedY){
      TreeMap<String, String> matches = new TreeMap<String, String>();
      Set<String> processed = new HashSet<String>();
      Queue<String> nodes = new LinkedList<String>();

      for(String x: unmatchedX){
        nodes.add(x);
      }

      while(!nodes.isEmpty()){
        String nodeBegin = nodes.element();
        processed.add(nodeBegin);
        if(edges.keySet().contains(nodeBegin)){
          for(String nodeEnd : edges.get(nodeBegin).getNodes()){
            nodes.remove(nodeBegin);
            if(!processed.contains(nodeEnd)){
              nodes.add(nodeEnd);
              matches.put(nodeBegin, nodeEnd);
              unmatchedX.remove(nodeBegin);
            }
          }
        }else if(inverseEdges.keySet().contains(nodeBegin)){
          for(String nodeEnd : inverseEdges.get(nodeBegin).getNodes()){
            nodes.remove(nodeBegin);
            if(!processed.contains(nodeEnd)){
              nodes.add(nodeEnd);
              matches.put(nodeEnd, nodeBegin);
              unmatchedY.remove(nodeBegin);
            }
          }
        }
        if (unmatchedX.isEmpty() && unmatchedY.isEmpty()){
          break;
        }
      }
      return matches;
    }


  public static TreeMap<String, String> BFS(TreeMap<String, Edge> edges){
    Set<String> nodesYMatched = new HashSet<String>();
    TreeMap<String, String> matches = new TreeMap<String, String>();
    Set<String> nodes = edges.keySet();

    for(String nodeX: nodes){
      if(!matches.containsKey(nodeX)){
        for(String nodeY:edges.get(nodeX).getNodes()){
          if(nodesYMatched.add(nodeY)){
            matches.put(nodeX, nodeY);
            break;
          }
        }
      }
    }

    return matches;
  }

  public static void addEdge(TreeMap<String, Edge> edges, TreeMap<String, Edge> inverseEdges, String[] pair){
    String nodeX = pair[0];
    String nodeY = pair[1];

   if (!edges.containsKey(nodeX)){
     edges.put(nodeX, new Edge(nodeY));
   }else{
     edges.get(nodeX).getNodes().add(nodeY);
   }
    if (!inverseEdges.containsKey(nodeY)){
     inverseEdges.put(nodeY, new Edge(nodeX));
   }else{
     inverseEdges.get(nodeY).getNodes().add(nodeX);
   }  
  }


	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String[] test = new String[] { "b->1", "b->4", "e->7", "e->3", "e->6", 
				"j->2", "j->5","j->4", "l->7", "l->2","t->7", "t->6","t->5", "a->3", "a->6", "r->6", "r->7"};
		System.out.print(BipartiteMatching(test));
	}

}
