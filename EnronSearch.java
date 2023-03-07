import java.util.*;
import javax.mail.*;
import javax.mail.internet.AddressException;

// SearchHandler
public class EnronSearch {


    // performs the term search
    private static void termSearch(String[] args) throws Exception {
        // verify the command line arguments
        if (args.length < 2) {
            System.out.println("Usage: java EnronSearch term_search term [term ...]");
            System.exit(1);
        }

        // convert the search terms to lower case and remove duplicates
        Set<String> searchTerms = new HashSet<>();
        for (int i = 1; i < args.length; i++) {
            searchTerms.add(args[i].toLowerCase());
        }

        // count the number of matching messages
        int count = 0;

        // create a session
        Session session = Session.getDefaultInstance(new Properties());
        // create a store
        Store store = session.getStore(new URLName("mbox:"));
        // connect to the store
        store.connect();

        // iterate through all folders under "enron/"
        for (Folder folder : store.getFolder("enron").list()) {
            // open the folder
            folder.open(Folder.READ_ONLY);
            // get the messages
            Message[] messages = folder.getMessages();
            // iterate over the messages
            for (Message message : messages) {
                // get the content and convert it to lower case
                String[] words = message.getContent().toString().toLowerCase().trim().replaceAll("\r\n", " ").split(" ");
                List<String> contentWords = new ArrayList<>();
                for (String word : words) {
                    // remove leading and trailing whitespaces
                    word.trim();
                    // drop empty strings
                    if (!word.isEmpty()) {
                        contentWords.add(word);
                    }
                }
                // check if the content contains all the search terms
                boolean containsAll = true;
                for (String term : searchTerms) {
                    if (!contentWords.contains(term)) {
                        containsAll = false;
                        break;
                    }
                }
                // print the subject if the message contains all the search terms
                if (containsAll) {
                    count++;
                    System.out.printf("%d. %s %s\n", count, addressString(message.getFrom()), message.getSentDate().toString());
                }
            }

            // close the folder
            folder.close(false);
        }

        // log the number of matching messages
        System.out.printf("Results found: %d\n", count);

        // close the store
        store.close();
    }


    // performs the address search
    private static void addressSearch(String[] args) throws Exception {
        // verify the command line arguments
        if (args.length < 3) {
            System.out.println("Usage: java EnronSearch address_search last_name first_name");
            System.exit(1);
        }

        // convert the search terms to lower case
        String lastName = args[1].toLowerCase();
        String firstName = args[2].toLowerCase();

        // count the number of matching messages
        int count = 0;

        // create a session
        Session session = Session.getDefaultInstance(new Properties());
        // create a store
        Store store = session.getStore(new URLName("mbox:"));
        // connect to the store
        store.connect();

        // iterate through all folders under "enron/"
        for (Folder folder : store.getFolder("enron").list()) {
            // open the folder
            folder.open(Folder.READ_ONLY);
            // get the messages
            Message[] messages = folder.getMessages();
            // iterate over the messages
            for (Message message : messages) {
                // check if the sender contains the search terms
                Address[] senders = getSenders(message);
                for (Address sender : senders) {
                    String senderString = sender.toString().toLowerCase();
                    if (senderString.contains(lastName) && senderString.contains(firstName)) {
                        count++;
                        System.out.printf("%d. %s -> %s [Subject: %s] %s\n", count, senderString, addressString(getRecipients(message)), message.getSubject(), message.getSentDate());
                        break;
                    }
                }

                // check if the recipients contain the search terms
                Address[] recipients = getRecipients(message);
                for (Address recipient : recipients) {
                    String recipientString = recipient.toString().toLowerCase();
                    if (recipientString.contains(lastName) && recipientString.contains(firstName)) {
                        count++;
                        System.out.printf("%d. %s -> %s [Subject: %s] %s\n", count, addressString(getSenders(message)), recipientString, message.getSubject(), message.getSentDate());
                        break;
                    }
                }
            }
        }

        // log the number of matching messages
        System.out.printf("Results found: %d\n", count);
        // close the store
        store.close();
    }

    // performs the interaction search
    private static void interactionSearch(String[] args) throws Exception {
        // verify the command line arguments
        if (args.length < 3) {
            System.out.println("Usage: java EnronSearch interaction_search address1 address2");
            System.exit(1);
        }

        // convert the search terms to lower case
        String address1 = args[1].toLowerCase();
        String address2 = args[2].toLowerCase();

        // count the number of matching messages
        int count = 0;
        // create a session
        Session session = Session.getDefaultInstance(new Properties());
        // create a store
        Store store = session.getStore(new URLName("mbox:"));
        // connect to the store
        store.connect();

        // iterate through all folders under "enron/"
        for (Folder folder : store.getFolder("enron").list()) {
            // open the folder
            folder.open(Folder.READ_ONLY);
            // get the messages
            Message[] messages = folder.getMessages();
            // iterate over the messages
            for (Message message : messages) {
                // check if this is a communication between the two addresses
                if (isCommunicationFrom(address1, address2, message)) {
                    count++;
                    System.out.printf("%d. %s -> %s [Subject: %s] %s\n", count, address1, address2, message.getSubject(), message.getSentDate().toString());
                } else if (isCommunicationFrom(address2, address1, message)) {
                    count++;
                    System.out.printf("%d. %s -> %s [Subject: %s] %s\n", count, address2, address1, message.getSubject(), message.getSentDate().toString());
                }
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        // verify the command line arguments
        if (args.length < 1) {
            System.out.println("Usage: java EnronSearch <search type> <args>");
            System.exit(1);
        }

        // convert the search type to lower case
        String searchType = args[0].toLowerCase();

        // take decision based on the search type
        switch (searchType) {
            case "term_search":
                termSearch(args);
                break;
            case "address_search":
                addressSearch(args);
                break;
            case "interaction_search":
                interactionSearch(args);
                break;
            default:
                System.out.println("Invalid search type");
                System.exit(1);
        }
    }



    // helper methods
    private static String addressString(Address[] addresses) {
        StringBuilder builder = new StringBuilder();
        for (Address address : addresses) {
            builder.append(address.toString());
            builder.append(" ");
        }
        return builder.toString().trim();
    }
    private static boolean isCommunicationFrom(String address1, String address2, Message message) throws Exception {
        Address[] senders = getSenders(message);
        Address[] recipients = getRecipients(message);

        // check if the sender is present
        boolean containsSender = false;
        for (Address sender : senders) {
            String senderString = sender.toString().toLowerCase();
            if (senderString.equals(address1)) {
                containsSender = true;
                break;
            }
        }

        if (!containsSender) {
            return false;
        }

        // check if the recipient is present
        boolean containsRecipient = false;
        for (Address recipient : recipients) {
            String recipientString = recipient.toString().toLowerCase();
            if (recipientString.equals(address2)) {
                containsRecipient = true;
                break;
            }
        }

        return containsRecipient;
    }

    private static Address[] getSenders(Message message) throws Exception {
        try {
            Address[] senders = message.getFrom();
            if (senders != null) return senders;
            return new Address[]{};
        } catch (AddressException e) {
            return new Address[]{};
        }
    }

    private static Address[] getRecipients(Message message) throws Exception {
        try {
            Address[] recipients = message.getAllRecipients();
            if (recipients != null) return recipients;
            return new Address[]{};
        } catch (AddressException e) {
            return new Address[]{};
        }
    }
}
 
