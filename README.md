# signingdemo-java-client

Dette er en lab hvor du skal kalle en GET-tjeneste i et REST API som krever at du signerer requestene du sender. Vi bruker Apache CXF som rammeverk for håndtering av klient og signering. 

___

For å få det til må vi:  
* importere en keystore som inneholder den private nøkkelen
* laste inn privatnøkkel fra keystore-fil til minne
* legge til en CXF interceptor som signerer alle utgående kall
* legge til CXF interceptorer som logger request og response

___

1. Kopier keystore.jks inn i src/main/resources/
2. Lag et Keystore objekt og hent ut privatnøkkel med alias "signing-demo". Passord for både keystore og nøkkel er "pw":
      ```java
       KeyStore keyStore = KeyStore.getInstance("JKS");
       keyStore.load(ClassLoaderUtils.getResourceAsStream("keystore.jks", this.getClass()),
                    "pw".toCharArray());

       PrivateKey privateKey = (PrivateKey)keyStore.getKey("signing-demo", "pw".toCharArray());
3. Lag et MessageSigner objekt og bruk privatnøkkelen for keyId "signing-demo-v1"
   ```java
   MessageSigner messageSigner = new MessageSigner(keyId -> privateKey, "signing-demo-v1");
   createSignatureInterceptor.setMessageSigner(messageSigner);

4. Lag en klient som bruker signerings-interceptoren og går mot https://signingtest.azurewebsites.net/api/stuff
   ```java
   final String serviceURI = "https://signingtest.azurewebsites.net/api/stuff";
   WebClient client = WebClient.create(serviceURI, Collections.singletonList(createSignatureInterceptor));

5. Sett opp interceptorer som logger request og response:
   ```java
   ClientConfiguration config = WebClient.getConfig(client);
   config.getInInterceptors().add(new LoggingInInterceptor());
   config.getOutInterceptors().add(new LoggingOutInterceptor());

6. Sett Content-type header og gjør en GET request med klienten.
   ```java
   client.header("Content-type", "text/plain");
   Response response = client.get();
   String responseString = response.readEntity(String.class);