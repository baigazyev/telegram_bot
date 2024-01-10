package io.project.KitapChooseBot.service;
import io.project.KitapChooseBot.model.*;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import io.project.KitapChooseBot.config.BotConfig;
import io.project.KitapChooseBot.model.User;
import io.project.KitapChooseBot.model.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;



@Component
@Slf4j

public class TelegramBot extends TelegramLongPollingBot{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private ClubRepository clubRepository;
	
	final BotConfig config;
	
	
	static final String HELP_TEXT = "This bot helping for you to choose book! \n\n" + "You can execute commands from the menu button on the left typing.\n\n" + 
	"Type /start to see a welcome message\n\n" + 
	"Type /mydata to see data stored about youself\n\n" + 
	"Type /help to see this message again\n\n" + 
	"Бұл қазақша жазба, мақсатым қазақшаны көтере ала ма ботым?" ;
	
	static final String BOT_IDEA = "Бұл боттың мақсаты, адамдарға кітап таңдауға көмектесу. Әртүрлі топтар, оқырман клубтары өздеріне ұнаған кітап тізімдерін өз "
			+ "тобындағы жалпы кітаптардың тізіміне қосып, сол кітаптар тізімінің ішінен бір айда немесе аптада оқылуы кітапты кездейсоқ таңдай алады. Ешкімде реніш сезімі "
			+ "туындамас үшін жасалған. \n\n" + "Telegram бот арқылы өзіңіздің тобыңыздағы барлық кітаптар тізімін көре аласыз.\n\n" + 
			"Және де кітап клубының ендігі болатын кездесу орны мен уақытын таңдай отыра, өз клубыңыздағы адамдарға ескерте аласыз!";
	
	private String bookTitle = null;
	private String author = null;
	private int bookYear;
	private int number;
	private String password;
	private Boolean login = false;
	
	 private enum BotState {
	        START,
	        ADDBOOK,
	        WAITING_COMMAND,
	        WAITING_FOR_TITLE,
	        WAITING_FOR_AUTHOR,
	        WAITING_FOR_YEAR,
	        BOOK_ADDED,
	        NULL,
	        DELETE_BOOK,
	        CHECK_CLUB_NUMBER,
	        CHECK_CLUB_PASSWORD,
	        ADD_CLUB_NUMBER,
	        ADD_CLUB_PASSWORD
	    }

	 private BotState botState = BotState.START;
	
	public TelegramBot(BotConfig config) {
		this.config = config;
		List<BotCommand> listofCommands = new ArrayList<>();
		listofCommands.add(new BotCommand("/start", "get a welcome message"));
		listofCommands.add(new BotCommand("/mydata", "get your data stored"));
		listofCommands.add(new BotCommand("/deleteBook", "delete book"));
		listofCommands.add(new BotCommand("/help", "info how to use this bot"));
		listofCommands.add(new BotCommand("/settings", "set your preferences"));
		listofCommands.add(new BotCommand("/botidea", "see our idea"));
		listofCommands.add(new BotCommand("/addbook", "set your book"));
		listofCommands.add(new BotCommand("/showbooks", "show all books"));
		listofCommands.add(new BotCommand("/registerToClub", "register to reading club"));
//		listofCommands.add(new BotCommand("/deleteBook", "delete book"));
		try {
			this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
		}
		catch(TelegramApiException e) {
			 log.error("Error setting bot's command list: " + e.getMessage());
		}
		
		
		
	
	}

	@Override
	@Transactional
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
//            KeyboardButton keyboardButton = new KeyboardButton();
//            keyboardButton.setText("Start");

            switch (botState) {
            	
            	case START: 	
            		
            			if(messageText.equals("/start")) {
            				
            				registerUser(update.getMessage());
            				startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
            				sendMessage(chatId, "Please enter club number:");
            				botState = BotState.CHECK_CLUB_NUMBER;
            				sendMessage(chatId, botState.toString());
//            				showKeyboardButton(chatId, "");
//            				showKeyboardButton(chatId, "show");
//            				showKeyboardButton(chatId, "show");
//            				showKeyboardButton(chatId, "show");
//            				showKeyboardButton(chatId, "show");
//            				sendMessage(chatId, "exists: chekReaderOrNot" + (chatId));
                			
            			}
            			else {
            				sendMessage(chatId, "Please enter correct command!");
            				
            			}
            			break;
            		
////            			sendMessage(chatId, "Введите команду:");
//            			startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
//            			botState = BotState.WAITING_COMMAND;
            			
                case ADDBOOK:
                    
                    botState = BotState.WAITING_FOR_TITLE;
                    sendMessage(chatId, "Давайте добавим новую книгу.\nВведите название книги:");
                    break;
                    
                case WAITING_FOR_TITLE:
                    bookTitle = messageText;
                    
                    botState = BotState.WAITING_FOR_AUTHOR;
                    sendMessage(chatId, "Введите имя автора книги:");
                    break;
                case WAITING_FOR_AUTHOR:
                    author = messageText;
                    botState = BotState.WAITING_FOR_YEAR;
                    sendMessage(chatId, "Введите год выпуска книги:");
                    break;
                    
                case WAITING_FOR_YEAR:
                    try {
                    	bookYear = Integer.parseInt(messageText);
                        // Здесь вы можете сохранить книгу в вашем хранилище
                        botState = BotState.WAITING_COMMAND;
                        Book book = new Book(update.getMessage().getChat().getFirstName(), bookTitle, author, bookYear);
                    	bookRepository.save(book);
                    	
                    	if(bookRepository.existsBynameOfBook(bookTitle)) {
                    		sendMessage(chatId, "Book succesfully saved");
                    		log.info("book saved: " + book.getNameOfBook());
                    		log.info("book name: " + bookRepository.existsBynameOfBook(bookTitle));
                    		
                    	}
                        
//                        sendMessage(chatId, "Книга добавлена: " + bookTitle + " (Автор: " + author + ", Год: " + bookYear + ")");
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Пожалуйста, введите корректный год.");
                    }
                    sendMessage(chatId, botState.toString());
                    break;
                case DELETE_BOOK:
                	
                	String nameOfBook = messageText;
                	if(bookRepository.existsBynameOfBook(nameOfBook)) {
                		Book book = bookRepository.findByNameOfBook(nameOfBook);
                		bookRepository.delete(book);
//                		bookRepository.deleteBynameOfBook(nameOfBook);
                		sendMessage(chatId, "Book succesfully deleted!");
                		botState = BotState.WAITING_COMMAND;
                		break;
                	}
                	else {
                		sendMessage(chatId, "Please enter correct book, this book doesn't exist");
                		break;
                	}
                	
                case CHECK_CLUB_NUMBER:
                	
                	try {
                    	number = Integer.parseInt(messageText);
                        // Здесь вы можете сохранить книгу в вашем хранилище
                        
                        
                    	
                    	
                    	if(clubRepository.existsBynumber(number)) {
                    		sendMessage(chatId, "Please enter password:");
                    		botState = BotState.CHECK_CLUB_PASSWORD;
                    		
                    		
                    		
                    	}
                    	else {
                    		sendMessage(chatId, "Club doesn't exist");
                    		botState = BotState.WAITING_COMMAND;
                    	}
                        
//                        sendMessage(chatId, "Книга добавлена: " + bookTitle + " (Автор: " + author + ", Год: " + bookYear + ")");
                    } catch (NumberFormatException e) {
                        sendMessage(chatId, "Пожалуйста, введите корректный номер.");
                    }
                	break;
                	
                case CHECK_CLUB_PASSWORD:
                	Club club = clubRepository.findBynumber(number);
                	
                	if(messageText.equals(club.getPassword())) {
                		
                		this.login = true;
                		sendMessage(chatId, "You are log in");
                		botState = BotState.WAITING_COMMAND;
                	}
                	else {
                		sendMessage(chatId, "Password is not correct! ");
                	}
                	break;
                
                case ADD_CLUB_NUMBER:
                	
                	try {
                	
                		number = Integer.parseInt(messageText);
                		if(!clubRepository.existsBynumber(number)) {
                			botState = BotState.ADD_CLUB_PASSWORD;
                			sendMessage(chatId, "Please enter password for club(minimum 8 characters:");
                			break;
                			
                		}
                		else {
                			sendMessage(chatId, "Club already exists, please enter another number:");
                		}
                		
                	} catch (NumberFormatException e) {
                        sendMessage(chatId, "Пожалуйста, введите корректный номер:");
                    } 
                	break;
                
                case ADD_CLUB_PASSWORD:
                	password = messageText;
                	if(password.length() >= 8 && !password.isEmpty()){
                		Club club1 = new Club();
                		club1.setNumber(number);
                		club1.setPassword(password);
//                		number = 0;
                		password = "";
              		
                		clubRepository.save(club1);
                		log.info("Clubs: " + clubRepository.existsBynumber(number));
                		
                		if(clubRepository.existsBynumber(number)) {
                			sendMessage(chatId, "Club saved");
                			sendMessage(chatId, "Users: " + club1.getMembers());
                		}
                		else {
                			sendMessage(chatId, "Club is not saved!");
                		}
                	}
                	else {
                		sendMessage(chatId, "Please enter minimum 8 characters:");
                	}
                	break;
                	
                    
                    
                case WAITING_COMMAND:
                	if (messageText.equals("/help")) {
                		sendMessage(chatId, HELP_TEXT);
                		break;
                	}
                	else if (messageText.equals("/showbooks")) {
                		
                		sendMessage(chatId, bookRepository.findAll().toString());
                		
                		break;
                	}
                	else if (messageText.equals("/addbook")) {
                		botState = BotState.WAITING_FOR_TITLE;
                        sendMessage(chatId, "Давайте добавим новую книгу.\nВведите название книги:");
                        sendMessage(chatId, botState.toString());
                        break;
                	}
                	else if (messageText.equals("/start")) {
                		botState = BotState.START;
                		break;
                	}
                	else if (messageText.equals("/deleteBook")) {
                		botState = BotState.DELETE_BOOK;
                		sendMessage(chatId, "Напишите название книги: ");
                		break;
                	}
                	else if (messageText.equals("/addClub")) {
                		if(update.getMessage().getChat().getUserName() == null) {
                			sendMessage(chatId, "Sorry, you can not add club, you are not admin");
                    		
                		}
                		else if(update.getMessage().getChat().getUserName().equals("baigazzyev")) {
                			botState = BotState.ADD_CLUB_NUMBER;
                    		sendMessage(chatId, "Please enter club number that you want to add:");
                		}
                		else {
                			sendMessage(chatId, "Sorry, you can not add club, you are not admin");
                		}
                		
                		
                		break;
                		
                	}
                    
                
                	
                	
                	
                	
                
                    
//                case BOOK_ADDED:
//                	Book book = new Book(update.getMessage().getChat().getFirstName(), bookTitle, author, bookYear);
//                	bookRepository.save(book);
//                	
//                	if(bookRepository.existsBynameOfBook(bookTitle)) {
//                		sendMessage(chatId, "Book succesfully saved");
//                		log.info("book saved: " + book.getNameOfBook());
//                		log.info("book name: " + bookRepository.existsBynameOfBook(bookTitle));
//                		
//                	}
                	
        			
//                	sendMessage(chatId, "Книга добавлена: " + bookRepository.findBynameOfBook(bookTitle). + " (Автор: " + author + ", Год: " + bookYear + ")");
                	
//                	botState = BotState.WAITING_COMMAND;
//                	sendMessage(chatId, botState.toString());
//                	break;
//                    
                default:
                	sendMessage(chatId, "Command not recognized!");
                	break;
            }
        }
				
					
						
						
            }
	
//	
//	private boolean checkReaderOrNot(long chatId) {
//		if(clubRepository.findBychatId(chatId) != null) {
//			return true;
//		}
//		return false;
//	}
            

		

	
	private boolean registerUser(Message msg) {
		
		if(userRepository.findById(msg.getChatId()).isEmpty()) {
			
			
			var chatId = msg.getChatId();
			var chat = msg.getChat();
			
			User user = new User();
			
			user.setChatId(chatId);
			user.setFirstName(chat.getFirstName());
			user.setLastName(chat.getLastName());
			user.setUserName(chat.getUserName());
			user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
			
			userRepository.save(user);
			
			log.info("user saved: " + user);
			return false;
			
			
			
		}
		else {
			return true;
		}
		
		
	}
	
		

	private void startCommandReceived(long chatId, String name) {
		
		
		String answer = "Hi, " + name + "!" + "\n" + "I'm helping to you choose book." + "\n" + "Введите команду:";
		log.info("Replied to user " + name);
		
		sendMessage(chatId, answer);
		
	}
	
	
	
	private void sendMessage(long chatId, String textToSend) {
		SendMessage message = new SendMessage();
		message.setChatId(String.valueOf(chatId));
		message.setText(textToSend);
		
		try{
			execute(message);
			
		}
		catch (TelegramApiException e) {
			log.error("Error occurred: " + e.getMessage());
		}
	}
	
	private void showKeyboardButton(long chatId, String string) {
		
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText("How are you reader?");
		sendMessage.setParseMode(ParseMode.MARKDOWN);
		sendMessage.setChatId(chatId);
		
		
		ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
		replyKeyboardMarkup.setResizeKeyboard(true);
		List<KeyboardRow> keyboardRowList = new ArrayList<>();
		KeyboardRow keyboardRow1 = new KeyboardRow();
		KeyboardButton keyboardButton = new KeyboardButton();
		keyboardButton.setText(string);
		keyboardRow1.add(keyboardButton);
		keyboardRowList.add(keyboardRow1);
		replyKeyboardMarkup.setKeyboard(keyboardRowList);
		sendMessage.setReplyMarkup(replyKeyboardMarkup);
		try {
			execute(sendMessage);
		}
		catch(TelegramApiException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return config.getBotName();
	}
	
	
	@Override
	public String getBotToken() {
		return config.getToken();
	}




}
