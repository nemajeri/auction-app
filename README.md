
# **Deployment Manual for Auction App**

## **Pre-requisites:**

- Java 11 or later installed
- Maven installed
- PostgreSQL installed
- RabbitMQ installed
- AWS S3 bucket
- Google OAuth2 application
- Facebook OAuth2 application
- Stripe account with API keys

## **Steps to Deploy:**

1. Clone the repository with the command:

       git clone <repository-url>

### **Back-end:**

2. Navigate into the project's root directory with the command:

       cd <directory-name>

3. Update the `application.properties` file with your specific details as follows:
   

>       spring.datasource.url=<your PostgreSQL database URL>
>       spring.datasource.username=<your PostgreSQL username>
>       spring.datasource.password=<your PostgreSQL password>
>       app.jwtSecret=<a secure random secret for JWT>
>       spring.security.oauth2.client.registration.google.client-id=<your Google OAuth2 application's client ID>
>       spring.security.oauth2.client.registration.google.client-secret=<your Google OAuth2 application's client secret>
>       spring.security.oauth2.client.registration.facebook.client-id=<your Facebook OAuth2 application's client ID>
>       spring.security.oauth2.client.registration.facebook.client-secret=<your Facebook OAuth2 application's client secret>
>       cloud.aws.credentials.access-key=<your AWS S3 bucket's access key>
>       cloud.aws.credentials.secret-key=<your AWS S3 bucket's secret key>
>       app.s3.bucket=<your AWS S3 bucket's name>
>       stripe.api-key=<your Stripe secret key>
>       spring.rabbitmq.host=<your RabbitMQ host>
>       spring.rabbitmq.port=<your RabbitMQ port>
>       spring.rabbitmq.username=<your RabbitMQ username>
>       spring.rabbitmq.password=<your RabbitMQ password>

4. Use Maven to build the application with the command:

       mvn clean install

5. After the build process completes, navigate to the target directory with the command:

       cd target

6. Start the application with the command:

       java -jar <jar-name>.jar

### **Front-end:**

2. Navigate into the project's root directory with the command:

       cd <directory-name>

3. Update the `.env` file with your specific details as follows:
   

>        REACT_APP_BASE_URL=<your back-end API's base URL>
>        REACT_APP_HOME_URL=<your front-end application's URL>
>        REACT_APP_GOOGLE_CLIENT_ID=<your Google OAuth2 application's client ID>
>        REACT_APP_FACEBOOK_APP_ID=<your Facebook OAuth2 application's client ID>
>        REACT_APP_STRIPE_PUBLISHED_KEY=<your Stripe published key>

4. Install all dependencies with the command:

       npm install

5. Start the application with the command:

       npm start
