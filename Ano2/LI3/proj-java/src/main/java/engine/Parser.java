package engine;

import engine.User;
import engine.Post;
import engine.StackOverflow;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

public class Parser{

	public void parseUser(StackOverflow of, String fileName){

		try{

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler(){
				public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException {
					User user = new User();
					if(attributes.getValue("Id")!=null){
						user.setIdU(Long.parseLong(attributes.getValue("Id")));
						user.setBio(attributes.getValue("AboutMe"));
						user.setName(attributes.getValue("DisplayName"));
						user.setReputation(Integer.parseInt(attributes.getValue("Reputation")));
						of.insertUser(user);
					}
				}
			};

			saxParser.parse(fileName,handler);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void parsePost(StackOverflow of, String fileName){

		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler(){
				public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException {
			
					if(attributes.getValue("Id")!=null){
						try{
						int type = Integer.parseInt(attributes.getValue("PostTypeId"));
						if(type==1 || type == 2){
							Post p = new Post();

							p.setTypePost(type);
							p.setIdPost(Long.parseLong(attributes.getValue("Id")));
							long idUser = Long.parseLong(attributes.getValue("OwnerUserId"));
						
							p.setData(xmlToDate(attributes.getValue("CreationDate")));
							p.setScore(Integer.parseInt(attributes.getValue("Score")));
							p.setNComments(Integer.parseInt(attributes.getValue("CommentCount")));
							p.setIdUser(idUser);
						
							of.increaseInfoUser(idUser);
						
							if(type == 1){
								//pergunta
								p.setIdAnswer(-1);
								p.setTitle(attributes.getValue("Title"));
								p.setTags(attributes.getValue("Tags"));
							}else if(type == 2){
								//resposta
								p.setIdAnswer(Long.parseLong(attributes.getValue("ParentId")));
							}
							//System.out.println(p.getData());
							of.insertPost(p);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}	
				}
			};
			saxParser.parse(fileName,handler);

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void parseTag(StackOverflow of, String fileName){
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler(){
				public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException {
					if(attributes.getValue("Id")!=null){
						try{
						Tag t = new Tag();
						t.setId(Long.parseLong(attributes.getValue("Id")));
						t.setTagName(attributes.getValue("TagName"));
						t.setCount(Long.parseLong(attributes.getValue("Count")));
						of.insertTag(t);

						}catch(Exception e){
							e.printStackTrace();
						}
					}	
				}
			};
			saxParser.parse(fileName,handler);

		}catch(Exception e){
			e.printStackTrace();
		}
	} 

	private LocalDate xmlToDate(String date){
		LocalDate d;
		int year,month,day;
		date = date.substring(0,10);
		year = Integer.parseInt(date.substring(0,4));
		month = Integer.parseInt(date.substring(5,7));
		day = Integer.parseInt(date.substring(8,10));

		d = LocalDate.of(year,month,day);
		return d;
	}

}