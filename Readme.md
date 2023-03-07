
# Computer Forensics Assignment-1

Computer Forensics Assignment-1 an email forensic to search the enron dataset



## Authors

- Santosh Gurajada(vgurajada@islander.tamucc.edu)
- Suraj()
- Praveen() 


## Prerequisites
Before you can compile and  run the Java file, you must ensure that the following software is installed on your system:

1.Java Development Kit (JDK): Version 8 or higher.
2.Activation library: Version 1.2.0 or higher.
3.JavaMail library: Version 1.5.6 or higher.
4.mbox library: Version 1.0.2 or higher.
5.Enron Dataset in the same folder as the Program.
## Compiling the Java File


1.Open a command prompt or terminal window on your system.

2.Navigate to the directory where the Java file is located.

3.Enter the following command to compile the Java file:
```bash
  javac -cp .:lib/javax.mail-1.5.6.jar:lib/mbox.jar:lib/javax.activation-1.2.0.jar EnronSearch.java
```
4.If the compilation is successful, you should see a new file called MyJavaFile.class in the same directory.
    

## Running the Java file

To run the Java file, follow these steps:

1.Ensure that the libraries are in the same directory as the Java file.

2.Enter the following command to run the Java file and perform a Address Search which will result in all emails sent and receive by a person:

```bash
 java -cp .:lib/javax.mail-1.5.6.jar:lib/mbox.jar:lib/javax.activation-1.2.0.jar EnronSearch address_search Last_Name First_Name
```
To perform Term Search with the Enron Dataset that results in the list of all terms present in the content of the email along with Sender address and Subject of the email.
```bash
 java -cp .:lib/javax.mail-1.5.6.jar:lib/mbox.jar:lib/javax.activation-1.2.0.jar EnronSearch term_search <terms>
```
To perform Interaction Search with Enron Dataset to get the E-mails sent and receive between two specified email addresses.

```bash
 java -cp .:lib/javax.mail-1.5.6.jar:lib/mbox.jar:lib/javax.activation-1.2.0.jar EnronSearch interaction_search email_address1 email_address2
```

## Screenshots

Term Search![Term Search](https://user-images.githubusercontent.com/127173737/223366786-61fa522b-58fd-49b7-a0ac-050a9f1ad95c.png)

Interaction Search![Interaction Search](https://user-images.githubusercontent.com/127173737/223367099-69bfa7e9-7097-4693-b6a5-d1f3f259e361.png)

Address Search![Address Search](https://user-images.githubusercontent.com/127173737/223367256-0494e53e-67e0-456f-83a4-3fe5d1593c5d.png
)
