package engine;

import li3.TADCommunity;
import java.util.Collections;
import java.time.LocalDate;
import java.text.ParseException;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Comparator;
import java.util.stream.Collectors;;
import common.Pair;

public class StackOverflow implements TADCommunity {
    // Variáveis de instância 
    private Map<Long, User> users;   			// Mapeamento entre os users e o seu ID
    private Map<LocalDate, List<Post>> posts;   // Mapeamento entre os posts e a sua data de criaçao
    private Map<Long,Tag> tags;

    public StackOverflow(){
        this.users = new HashMap<>();
        this.posts = new TreeMap<LocalDate,List<Post>>(new ComparadorDeDatas());
        this.tags = new HashMap<>();
    }
    
    public StackOverflow(Map<Long, User> u, Map<LocalDate, List<Post>> p){
        this.users = u;
        this.posts = p;
    }
    
    public StackOverflow(StackOverflow s){
        this.users = s.getUsers();
        this.posts = s.getPosts();
    }
    
    
    public Map<Long, User> getUsers(){
        return users.values().stream()
        						.collect(Collectors.toMap(User::getIdU, User::clone));
    }
    
    public Map<LocalDate, List<Post>> getPosts(){
         Map<LocalDate,List<Post>> mapa = new TreeMap<>();
         for(LocalDate e: posts.keySet()){
         	List<Post> p = new ArrayList<Post>();
         	p = posts.get(e);
         	mapa.put(e,p);
         }
         return mapa;
	}

	public Map<Long,Tag> getTags(){
        return this.tags.values().stream()
                                  .collect(Collectors.toMap(Tag::getId, Tag::clone));
	}
	
	public void setUsers(Map<Long, User> u) {
        this.users= u;
    }
    
    public void setPosts(Map<LocalDate, List<Post>> p) {
        this.posts= p;
	}

	public void setTags(Map<Long,Tag> t){
        this.tags = t;
	}
	
	public boolean equals (Object e) {
        if(e == this)
            return true;
        if(e == null || e.getClass() != this.getClass())
            return false;
        StackOverflow s = (StackOverflow) e;
        return (this.users == s.getUsers() &&
                this.posts == s.getPosts() &&
                this.tags == s.getTags());
	}
	
	public StackOverflow clone () {
        return new StackOverflow(this);
	}
	
	public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("Users: ");
        for (User u : users.values()) {
            sb.append(u.toString());
        }
        sb.append("\n");
        sb.append("Posts: ");
        for (List<Post> l : this.posts.values()) {
            for(Post p:l)
            	sb.append(p.toString());
        }
        sb.append("\n");
        sb.append("Tags: ");
        for (Tag t : this.tags.values()) {
            sb.append(t.toString());
        }
        sb.append("\n");
        return sb.toString();
	}
	

	public void load(String filename){
		Parser p = new Parser();
		p.parseUser((this),filename+"/Users.xml");
		p.parsePost((this),filename+"/Posts.xml");
		//p.parseTag((this),filename+"/Tags.xml");
	}

	public Post getPost(LocalDate date, long id){
		if(posts.containsKey(date)){
			for(Post p:posts.get(date)){
				if(p.getIdPost()==id)
					return p.clone();
			}
		}
		return null;
	}

	public User getUser(long id){
		if(users.containsKey(id))
			return users.get(id).clone();
		return null;
	}
	
	public void insertUser(User u){
		users.put(u.getIdU(),u.clone());
	}
	
	public void insertPost(Post p){
		if(!posts.containsKey(p.getData())){
			List<Post> aux = new ArrayList<>();
			aux.add(p.clone());
			posts.put(p.getData(),aux);
			return;
		}
		posts.get(p.getData()).add(p.clone());
	}

	public void insertTag(Tag t){
		tags.put(t.getId(),t.clone());
	}
	

	public void increaseInfoUser(long id) throws UserInexisteException,ParseException{
		if(!(users.containsKey(id))){
			throw new UserInexisteException();
		}else{
			User u = users.get(id);
			u.setNumPosts(u.getNumPosts() + 1);
		}
	}

	public List<Post> getPostsUser(long id){
		List<Post> aux = new ArrayList<Post>();
		for(List<Post> pps:posts.values()){
			for(Post p:pps){
				if(p.getIdUser()==id){
					aux.add(p.clone());
				}
			}
		}
		return aux;
	}

	public List<Long> ordenaArrays(List<LocalDate> lastNTime,List<Long> lastN){
		for(int i=0;i<lastNTime.size()-1;i++){
			//last element are already in place
			for(int j=0;j<lastNTime.size()-i-1;j++){
				LocalDate aux = lastNTime.get(j);
				Long id = lastN.get(j);

				if(aux.isBefore(lastNTime.get(j+1)) || aux.equals(lastNTime.get(j+1))){
					lastNTime.set(j,lastNTime.get(j+1));
					lastNTime.set(j+1,aux);

					lastN.set(j,lastN.get(j+1));
					lastN.set(j+1,id);
				}
			}
		}
		return lastN;
	}

	// Query 1
    public Pair<String,String> infoFromPost(long id) {
    	String title = null;
    	String name = null;
		for(List<Post> p:this.posts.values()){
			for(Post post:p){
				if(post.getIdPost()==id){
					if(post.getTypePost()==1){
						title = post.getTitle();
						name = getUser(post.getIdUser()).getName();		
					}else{
						Post aux = getPost(post.getData(),post.getIdAnswer());
						title = aux.getTitle();
						name = getUser(aux.getIdUser()).getName();
					}
				}
			}
		}
        return new Pair<>(title,name);
    }

    //Querie 2
    public List<Long> topMostActive(int N) {
        List<User> us = new ArrayList<User>();
        List<Long> aux = new ArrayList<Long>();
        for(User u:this.users.values()){
        	us.add(u.clone());
        }
        Collections.sort(us,new ComparatorNumPosts());
        Collections.reverse(us);
        us = us.subList(0,N);
        for(User u: us){
        	aux.add(u.getIdU());
        }
        return aux;
    }
    
    // Query 3
    public Pair<Long,Long> totalPosts(LocalDate begin, LocalDate end) {
        long fst = 0;long snd = 0;
        for(LocalDate d: this.posts.keySet()){
        	if((d.isAfter(begin) && d.isBefore(end)) || d.equals(begin) || d.equals(end)){
        	for(Post p:posts.get(d)){
        		if(p.getTypePost() == 1){
        			fst++;
        			}else
        				snd++;
        		}
        	}
    	}
    	return new Pair<>(fst,snd);
    }

    // Query 4
    public List<Long> questionsWithTag(String tag, LocalDate begin, LocalDate end) {
        List<Long> aux = new ArrayList<Long>();
        for(LocalDate d: this.posts.keySet()){
        	if((d.isAfter(begin) && d.isBefore(end)) || d.equals(begin) || d.equals(end)){
        	for(Post p:this.posts.get(d)){
        		if(p.getTypePost() == 1 && p.getTags().contains(tag)){
        			aux.add(p.getIdPost());
        			}
        		}
        	}
    	}
        Collections.reverse(aux);
        return aux;
    }

    // Query 5
    public Pair<String, List<Long>> getUserInfo(long id) {
        User u = new User();
        String bio = null;
        List<Long> idsPosts = new ArrayList<Long>();
        List<LocalDate> datasPosts = new ArrayList<LocalDate>();
        List<Post> p = new ArrayList<Post>();
        List<Long> idsFinal = new ArrayList<Long>();

        u = getUser(id);bio = u.getBio();
        p = getPostsUser(id);

        for(Post pp: p){
        	idsPosts.add(pp.getIdPost());
        	datasPosts.add(pp.getData());
        }
        idsFinal = ordenaArrays(datasPosts,idsPosts).subList(0,10);
        Pair <String,List<Long>> pair = new Pair<>(bio,idsFinal);
        return pair;
    }

    // Query 6
    public List<Long> mostVotedAnswers(int N, LocalDate begin, LocalDate end) {
        List<Post> postsRespostas = new ArrayList<Post>();
        List<Long> idsRespostas = new ArrayList<Long>();

        postsRespostas = getPostsRespostas(begin,end);
        Collections.sort(postsRespostas, new ComparatorVotos());
        for(int i=0;i<N;i++){
        	idsRespostas.add(postsRespostas.get(i).getIdPost());
        }
        return idsRespostas;
    }

    public List<Post> getPostsRespostas(LocalDate begin, LocalDate end){
    	List<Post> postsRespostas = new ArrayList<Post>();
    	for(LocalDate e: this.posts.keySet()){
    		if((e.isAfter(begin) && e.isBefore(end)) || e.equals(begin) || e.equals(end)){
    			for(Post p:this.posts.get(e)){
    				if(p.getTypePost() == 2){
    					postsRespostas.add(p.clone());
    				}
    			}
    		}
    	}
    	return postsRespostas;
    }
    
    // Query 7
    public List<Long> mostAnsweredQuestions(int N, LocalDate begin, LocalDate end) {
        Map<Long,Integer> aux = new HashMap<Long,Integer>();
        //perguntas entre a data
        for(LocalDate e:this.posts.keySet()){
        	if((e.isAfter(begin) && e.isBefore(end)) || e.equals(begin) || e.equals(end)) {        		
        		for(Post p:this.posts.get(e)){
        			if(p.getTypePost() == 1 && !aux.containsKey(p.getIdPost())){
        				aux.put(p.getIdPost(),0);
        			}
        		}
        	}
        }
        //correr todos os posts e nao so naquela data
        for(List<Post> p:posts.values()){
        	for(Post post:p){
        		if(post.getTypePost()==2 && aux.containsKey(post.getIdAnswer())){
        			aux.put(post.getIdAnswer(),aux.get(post.getIdAnswer())+1);
        		}
        	}
        }
        //System.out.println(entriesSortedByValues(aux));
        List <Long> list = entriesSortedByValues(aux).stream().map(Map.Entry::getKey).collect(Collectors.toList());

    	return list.subList(0,N);
    }

    public List<Map.Entry<Long,Integer>> entriesSortedByValues(Map<Long,Integer> map){

    	List<Map.Entry<Long,Integer>> sortedEntries = new ArrayList<Map.Entry<Long,Integer>>(map.entrySet());

    	Collections.sort(sortedEntries,
    		new Comparator<Map.Entry<Long,Integer>>(){
    			public int compare(Map.Entry<Long,Integer> e1, Map.Entry<Long,Integer> e2){
    				return e2.getValue().compareTo(e1.getValue());
    			}
    		}
    	);
    	return sortedEntries;
    	}

    // Query 8
    public List<Long> containsWord(int N, String word) {
        List<Long> idsPosts = new ArrayList<Long>();
        List<LocalDate> datasPosts = new ArrayList<LocalDate>();
        List<Post> p = new ArrayList<Post>();
        List<Long> idsFinal = new ArrayList<Long>();

        p = getWords(word);
        for(Post post:p){
        	idsPosts.add(post.getIdPost());
        	datasPosts.add(post.getData());
        }
        idsFinal = ordenaArrays(datasPosts,idsPosts).subList(0,N);
        return idsFinal;
    }

    public List<Post> getWords(String word){
    	List<Post> aux = new ArrayList<Post>();
    	for(LocalDate e:this.posts.keySet()){
    		for(Post p:this.posts.get(e)){
    			if(p.getTitle().contains(word))
    				aux.add(p.clone());
    		}
    	}
    	return aux;
    }

    // Query 9
    public List<Long> bothParticipated(int N, long id1, long id2) {
        List<Long> posts1 = new ArrayList<Long>();
        List<Long> posts2 = new ArrayList<Long>();
        List<Long> aux = new ArrayList<Long>();
        posts1 = getAll(id1);
        posts2 = getAll(id2);
        if(posts1.size() < posts2.size()){
        	for(Long id: posts1){
        		if(posts2.contains(id))
        			aux.add(id);
        	}
        }else{
        	for(Long id:posts2){
        		if(posts1.contains(id))
        			aux.add(id);
        	}
        }
        return aux;
    }

    //salientar que pode ter mais do que um post relativo a mesma pergunta 
    public List<Long> getAll(long id){
    	List<Long> aux = new ArrayList<Long>();
    	for(LocalDate e:this.posts.keySet()){
    		for(Post p:this.posts.get(e)){
    			if(p.getTypePost() == 1 && p.getIdUser() == id && !aux.contains(p.getIdPost()))
    				aux.add(p.getIdPost());
    			else if(p.getTypePost() == 2 && p.getIdUser() == id && !aux.contains(p.getIdAnswer()))
    				aux.add(p.getIdAnswer());
    		}
    	}
    	return aux;
    }

    // Query 10
    public long betterAnswer(long id) {
        double max = -1;
        long idBest = -1;

        for(List<Post> post:this.posts.values()){
        	for(Post p:post){
        		if(p.getTypePost()==2 && p.getIdAnswer()==id){
        			double aux = (p.getScore() * 0.45) + (getUser(p.getIdUser()).getReputation() * 0.25) + (p.getScore() * 0.2) + (p.getNComments() * 0.1);
        			if(aux > max){
        				max = aux; idBest = p.getIdPost();
        			}
        		}
        	}
        }
        return idBest;
    }

    // Query 11
    public List<Long> mostUsedBestRep(int N, LocalDate begin, LocalDate end) {
    	return null;
    }

    public void clear(){
    	this.users.clear();
    	this.posts.clear();
    }
}