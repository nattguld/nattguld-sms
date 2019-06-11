# nattguld-sms
Handles SMS receiving &amp; handling using 3rd party services

## Dependencies
This library uses the following dependencies:  
**nattguld-http:** Used for HTTP communication. https://github.com/randqm/nattguld-http  
**nattguld-util:** Contains various helper methods. https://github.com/randqm/nattguld-util  
**nattguld-data:** For managing data & configs. https://github.com/randqm/nattguld-data  

## About
Requests phone numbers and retrieves SMS's through 3rd party services.

## Setup configurations
Before using this library configurations must be set so we know which 3rd party service to use and have the required details such as an API key.
```java
public static void main(String[] args) {
    (SMSConfig)ConfigManager.getConfig(new SMSConfig()).update(SMSProvider.EXAMPLE, username, apiKey);
}
```

## Example usage
```java
try (SMSTask task = new SMSTask(Platform.EXAMPLE)) {
  SMSNumber sn = task.requestSMSNumber();

  if (Objects.isNull(sn)) {
      System.err.println("No number received");
      return;
  }
  //Do stuff with sn.getNumber();

  String sms = task.retrieveSMS();

  if (Objects.nonNull(sms)) {
      //Do stuff with SMS
  }
}
```
