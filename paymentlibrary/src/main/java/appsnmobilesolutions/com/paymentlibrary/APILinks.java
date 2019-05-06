package appsnmobilesolutions.com.paymentlibrary;

public interface APILinks {

    public String SERVER_URL = "https://payments.anmgw.com";
//    public String SERVER_URL = "http://5.153.40.138:7015";

    public String SEND_REQUEST_URL = SERVER_URL+"/third_party_request";
    public String PAYMENT_PAGE_URL = SERVER_URL+"/payment_page?code=";

//    https://payments.anmgw.com/payment_page?code=dG9rZW49ODk5MTgxMjEwMTMzMzM4MyZ0cmFuc19pZD13Y19vcmRlcl81YmY1
}
