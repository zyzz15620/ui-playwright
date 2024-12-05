package data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormTestData {
    public static String FORM_VALIDATION_DATA = """
            [
              {
                "Full Name": {
                  "input": "",
                  "expected": "Please input your full name!"
                },
                "Email": {
                  "input": "",
                  "expected": "Please input your email!"
                },
                "Phone Number": {
                  "input": "",
                  "expected": "Please input your phone number!"
                },
                "Date of Birth": {
                  "input": "",
                  "expected": "Please select your date of birth!You must be at least 18 years old!"
                },
                "Address": {
                  "input": "",
                  "expected": "Please input your address!"
                }
              },
              {
                "Full Name": {
                  "input": "",
                  "expected": "Please input your full name!"
                },
                "Email": {
                  "input": "abc.com",
                  "expected": "The input is not valid E-mail!"
                },
                "Phone Number": {
                  "input": "123456789",
                  "expected": "Phone number must be 10 digits!"
                },
                "Date of Birth": {
                  "input": "2024-12-03",
                  "expected": "You must be at least 18 years old!"
                },
                "Address": {
                  "input": "",
                  "expected": "Please input your address!"
                }
              },
              {
                "Full Name": {
                  "input": "",
                  "expected": "Please input your full name!"
                },
                "Email": {
                  "input": "abc.com",
                  "expected": "The input is not valid E-mail!"
                },
                "Phone Number": {
                  "input": "12345678911",
                  "expected": "Phone number must be 10 digits!"
                },
                "Date of Birth": {
                  "input": "AAA",
                  "expected": "Please select your date of birth!You must be at least 18 years old!"
                },
                "Address": {
                  "input": "",
                  "expected": "Please input your address!"
                }
              }
            ]
            """;

    public static String FORM_VALID_DATA = String.format("""
            [
               {
                 "Full Name": "Pham Anh Duc",
                 "Email": "duc.total650@gmail.com",
                 "Phone Number": "0808008008",
                 "Date of Birth": "2000-01-01",
                 "Address": "HCM"
               },
               {
                 "Full Name": "total650",
                 "Email": "duc@edu.vn",
                 "Phone Number": "1111111111",
                 "Date of Birth": "%s",
                 "Address": "Please input your address!",
                 "Occupation": "meow",
                 "Company": "cat"
               }
             ]
            """, getValidBoundaryBirth());

    private static String getValidBoundaryBirth(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        String validBoundaryBirth = dateTimeFormatter.format(today.minusYears(18));
        return validBoundaryBirth;
    }
}
