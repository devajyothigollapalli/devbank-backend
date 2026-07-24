package com.b1Banking.ZenBanking.Controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.b1Banking.ZenBanking.DTO.CibilDTO;
import com.b1Banking.ZenBanking.DTO.CreditCardRequest;
import com.b1Banking.ZenBanking.DTO.ForgotPassword;
import com.b1Banking.ZenBanking.DTO.ForgotPin;
import com.b1Banking.ZenBanking.DTO.LoginRequest;
import com.b1Banking.ZenBanking.DTO.MiniPin;
import com.b1Banking.ZenBanking.DTO.RegisterRequest;
import com.b1Banking.ZenBanking.DTO.SetPinRequest;
import com.b1Banking.ZenBanking.DTO.TransferRequest;
import com.b1Banking.ZenBanking.DTO.UpdateUserDTO;
import com.b1Banking.ZenBanking.Entity.BankingEntity;
import com.b1Banking.ZenBanking.Entity.CreditCardEntity;
import com.b1Banking.ZenBanking.Entity.PdfEntity;
import com.b1Banking.ZenBanking.Exceptions.AccountNotFound;
import com.b1Banking.ZenBanking.Exceptions.AdminNotFound;
import com.b1Banking.ZenBanking.Exceptions.AgeValidation;
import com.b1Banking.ZenBanking.Exceptions.CreditLimitReached;
import com.b1Banking.ZenBanking.Exceptions.InactiveAccount;
import com.b1Banking.ZenBanking.Exceptions.IncorrectPIN;
import com.b1Banking.ZenBanking.Exceptions.InvalidAmount;
import com.b1Banking.ZenBanking.Exceptions.InvalidCredentials;
import com.b1Banking.ZenBanking.Exceptions.InvalidDOB;
import com.b1Banking.ZenBanking.Exceptions.InvalidOTP;
import com.b1Banking.ZenBanking.Exceptions.PANInvalid;
import com.b1Banking.ZenBanking.Exceptions.PinNotSetException;
import com.b1Banking.ZenBanking.Exceptions.ReceiverNotFound;
import com.b1Banking.ZenBanking.Exceptions.RoleMisMatch;
import com.b1Banking.ZenBanking.Exceptions.SameAccount;
import com.b1Banking.ZenBanking.Exceptions.SameTypeCreditCard;
import com.b1Banking.ZenBanking.Exceptions.SenderNotFound;
import com.b1Banking.ZenBanking.Exceptions.UnavailableBalance;
import com.b1Banking.ZenBanking.Exceptions.UserNotFound;
import com.b1Banking.ZenBanking.Repo.PdfRepo;
import com.b1Banking.ZenBanking.Services.AgeService;
import com.b1Banking.ZenBanking.Services.BankingServiceInt;
import com.b1Banking.ZenBanking.Services.CibilService;
import com.b1Banking.ZenBanking.Services.CreditCardService;
import com.b1Banking.ZenBanking.Services.MailService;
import com.b1Banking.ZenBanking.Services.MiniPdfService;
import com.b1Banking.ZenBanking.Services.NotifyService;
import com.b1Banking.ZenBanking.Services.OtpSender;
import com.b1Banking.ZenBanking.Services.TxnUtilService;

@RestController
@RequestMapping("/api")
// http://localhost:8080/api/fetch


public class BankingController  {
	@Autowired
private BankingServiceInt bs;
	 @Autowired
	    private MailService emailService;
	 @Autowired
	    private TxnUtilService txnUtil;
     @Autowired
	    private OtpSender otpService;
	 @Autowired
	     private       MiniPdfService mini;
	 @Autowired
	 private         NotifyService notify;
	 @Autowired
	 private        PdfRepo pdfrepo;
	 @Autowired
	 private         AgeService ageservice;
	 @Autowired
	 private        CreditCardService creditCardService;

	 @Autowired
	 private    PasswordEncoder encoder;
	 @Autowired
	 private CibilService cibilService;
	 @PostMapping("/fetch")
	 public String CreateNewAccount(@RequestBody RegisterRequest request) {

	     BankingEntity be = request.getUser();
	     if (!otpService.validateOTP(be.getMail(), request.getOtp())) {
	         throw new InvalidOTP("Invalid OTP");
	     }
	     if (be.getAge() == null) {
	         throw new AgeValidation("Date of Birth is required");
	     }
	     LocalDate dob = be.getAge()
	             .toInstant()
	             .atZone(ZoneId.systemDefault())
	             .toLocalDate();

	     int age = Period.between(dob, LocalDate.now()).getYears();

	     if (age < 0 || age >= 100) {
	         throw new AgeValidation("Invalid Age Entered: " + age);
	     }
	     if (age < 16) {
	         be.setAccountType("ZERO ACCOUNT");
	     } else {
	         be.setAccountType("SAVINGS ACCOUNT");
	     }

	     be.setPinNo(0);
	     be.setPanNo(null);
	     be.setPassword(encoder.encode(be.getPassword()));
	     be.setStatus("ACTIVE");
	     be.setBalance(0);

	     BankingEntity savedUser = bs.openNewAccount(be);

	    
	     emailService.sendMail(
	             savedUser.getMail(),
	             "DevBank Account Created Successfully",
	             "Dear " + savedUser.getAccountHolder() + ",\n\n" +
	             "Welcome to DevBank 🎉\n" +
	             "Account No: " + savedUser.getAccountNo() + "\n" +
	             "Account Type: " + savedUser.getAccountType() + "\n\n" +
	             "Please set your PIN after login.\n\n- DevBank"
	     );

	     return "Account Created Successfully 🎉 Account No: "
	             + savedUser.getAccountNo()
	             + " | Account Type: "
	             + savedUser.getAccountType();
	 }

//@GetMapping("/allusers")
//public List<BankingEntity>getAllUsers(){
//	return bs.getAllData();
//}
	 @GetMapping("/allusers/{empNo}")
	 public List<BankingEntity> getAllUsers(@PathVariable long empNo) {
	     BankingEntity admin = bs.getById(empNo)
	             .orElseThrow(() -> new AdminNotFound("Admin not found"));

	     if (!(admin.getRole().equalsIgnoreCase("CEO") ||
	           admin.getRole().equalsIgnoreCase("Founder") ||
	           admin.getRole().equalsIgnoreCase("Manager"))) {
	         throw new RoleMisMatch("You are not authorized to view all users");
	     }

	     return bs.getAllData();
	 }

//    http://localhost:8080/api/usingid/4
@GetMapping("/usingid/{id}")
public BankingEntity usingUserId(@PathVariable long id)throws UserNotFound {
return bs.getById(id).orElseThrow(()->new UserNotFound("Account not Found"));	
}
//@PutMapping("/depositCash/{id}")
//public BankingEntity deposit(@PathVariable long id,@RequestBody BankingEntity be)throws Exception {
//	BankingEntity ex_amount=bs.getById(id).orElseThrow(()->new UserNotFound("Account not found"));
//	checkAccountActive(ex_amount);
//if(be.getBalance()<=0) {
//	throw new InvalidAmount("Invalid Amount");
//}
//else {
//	double totalBalance=be.getBalance()+ex_amount.getBalance();
//	ex_amount.setBalance(totalBalance);
//}
//BankingEntity depnote= bs.depositedcash(ex_amount);
//double depositAmount = be.getBalance();
//txnUtil.saveTxn(
//	    depnote,
//	    "DEPOSIT",
//	    depositAmount,
//	    "Cash Deposit"
//	);
//
//emailService.sendMail(
//        depnote.getMail(),"ZenBank", "Dear "+depnote.getAccountHolder()+","+"\n"+"\n"+"A/c *000"+depnote.getAccountNo()+" Credited with Rs:"+be.getBalance()+"\nAvalable Balance: Rs:"+depnote.getBalance()+"\nIf not you, Call 1800222243 - ZenBank of India");
////notify.sendMiniStatement(depnote,"Deposit successful");
//        return depnote;
//}
//@PutMapping("/depositCash/{id}")
//public BankingEntity deposit(@PathVariable long id,
//                             @RequestBody BankingEntity be) {
//
//    BankingEntity ex_amount = bs.getById(id)
//            .orElseThrow(() -> new UserNotFound("Account not found"));
//
//    checkAccountActive(ex_amount);
//
//    validatePIN(ex_amount.getPinNo(), be.getPinNo());
//
//    double depositAmount = be.getBalance();
//
//    if (depositAmount <= 0) {
//        throw new InvalidAmount("Invalid Amount");
//    }
//
//    if (depositAmount > 200000) {
//        throw new InvalidAmount("Cannot deposit more than Rs. 2,00,000 at a time");
//    }
//
//    ex_amount.setBalance(ex_amount.getBalance() + depositAmount);
//
//    BankingEntity depnote = bs.depositedcash(ex_amount);
//
//    txnUtil.saveTxn(depnote, "DEPOSIT",
//            depositAmount, "Cash Deposit");
//    emailService.sendMail(
//        depnote.getMail(),"ZenBank", "Dear "+depnote.getAccountHolder()+","
//    +"\n your cibile score is"+depnote.getCibilScore()+"\n"+"A/c *000"+depnote.getAccountNo()
//    +" Credited with Rs:"+be.getBalance()
//    +"\nAvalable Balance: Rs:"+depnote.getBalance()
//    +"\nIf not you, Call 1800222243 - ZenBank of India");
//
//    return depnote;
//}

@PutMapping("/depositCash/{id}")
public BankingEntity deposit(@PathVariable long id,
                             @RequestBody BankingEntity be) {

    BankingEntity ex_amount = bs.getById(id)
            .orElseThrow(() -> new UserNotFound("Account not found"));

    checkAccountActive(ex_amount);

    validatePIN(ex_amount.getPinNo(), be.getPinNo());

    double depositAmount = be.getBalance();

    if (depositAmount <= 0) {
        throw new InvalidAmount("Invalid Amount");
    }

    if (depositAmount > 200000) {
        throw new InvalidAmount("Cannot deposit more than Rs. 2,00,000 at a time");
    }

    ex_amount.setBalance(ex_amount.getBalance() + depositAmount);

    BankingEntity depnote = bs.depositedcash(ex_amount);


    int cibilScore = cibilService.calculateCibil(depnote);


    txnUtil.saveTxn(depnote, "DEPOSIT",
            depositAmount, "Cash Deposit");

    emailService.sendMail(
        depnote.getMail(),
        "DevBank Transaction Alert",
        "Dear " + depnote.getAccountHolder() +
        ",\n\nYour CIBIL Score: " + cibilScore +
        "\nA/c *000" + depnote.getAccountNo() +
        " Credited with Rs: " + depositAmount +
        "\nAvailable Balance: Rs: " + depnote.getBalance() +
        "\n\nIf not you, Call 1800222243 - DevBank of India"
    );

    return depnote;
}

@PutMapping("/pinSet/{id}")
public String pinNo(@PathVariable long id,
                    @RequestBody BankingEntity req) {

    BankingEntity user = bs.getById(id)
            .orElseThrow(() -> new UserNotFound("No User Found"));

    checkAccountActive(user);

    int newPin = req.getPinNo();

    if (newPin <= 0 || String.valueOf(newPin).length() != 4) {
        throw new IncorrectPIN("PIN must be 4 digits");
    }

    user.setPinNo(newPin);

    bs.updateUser(user);

    return "PIN updated successfully";
}

@PutMapping("/pinSetWithOtp")
public String setPinWithOtp(@RequestBody SetPinRequest req) {

    BankingEntity user = bs.getById(req.getAccountNo())
        .orElseThrow(() -> new UserNotFound("User not found"));

    checkAccountActive(user);

  
    if (!user.getMail().equals(req.getMail())) {
        throw new InvalidCredentials("Email mismatch");
    }

  
    otpService.validateOTP(req.getMail(), req.getOtp());

   
    int newPin = req.getPinNo();
    if (newPin <= 0 || String.valueOf(newPin).length() != 4) {
        throw new IncorrectPIN("PIN must be 4 digits");
    }

    user.setPinNo(newPin);
    bs.updateUser(user);

    return "✅ PIN set successfully";
}

@PutMapping("/withdrawCash/{accountId}")
public double amountWithdraw(@PathVariable long accountId,
                             @RequestBody BankingEntity be) throws Exception {

    BankingEntity ex_be = bs.getById(accountId)
            .orElseThrow(() -> new UserNotFound("Account not found"));

    checkAccountActive(ex_be);

    validatePIN(ex_be.getPinNo(), be.getPinNo());

   
    double withdrawAmount = be.getBalance();

    if (withdrawAmount <= 0) {
        throw new InvalidAmount("Invalid withdrawal amount");
    }

    
    if (ex_be.getBalance() - withdrawAmount < 1000) {
        throw new UnavailableBalance("Minimum balance of 1000 must be maintained");
    }

 
    ex_be.setBalance(ex_be.getBalance() - withdrawAmount);

    BankingEntity withnote = bs.openNewAccount(ex_be);

   
    txnUtil.saveTxn(
            withnote,
            "WITHDRAW",
            withdrawAmount,
            "Cash Withdrawal"
    );

   
    emailService.sendMail(
            withnote.getMail(),
            "DevBank",
            "Dear " + withnote.getAccountHolder() + ",\n\n"
            		 + "A/c *000" + withnote.getAccountNo()
                     + " Debited with Rs:" + withdrawAmount
                     + "\nAvailable Balance: Rs:" + withnote.getBalance()
             );

             return withnote.getBalance();
         }

private boolean validatePIN(int prev_pin,int pin) {
	if(prev_pin==(pin)) {
		return true;
	}
	else {
		throw new IncorrectPIN("You entered Incorrect PIN");
	}
}

private void checkAccountActive(BankingEntity user) {
    if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
        throw new InactiveAccount(user.getAccountNo()+"Account is inactive. Please contact DevBank. Thank you");
    }
}


@PostMapping("/delusingrole/{empNo}")
public String delUsingRole(@PathVariable long empNo, @RequestBody BankingEntity be) {

    BankingEntity emp_be = bs.getById(empNo)
            .orElseThrow(() -> new AdminNotFound("Admin not found"));

    
    if (!(emp_be.getRole().equalsIgnoreCase("CEO")
            || emp_be.getRole().equalsIgnoreCase("Founder")
            || emp_be.getRole().equalsIgnoreCase("Manager"))) {

        throw new RoleMisMatch("You are not authorized to delete users");
    }

    
    if (!encoder.matches(be.getPassword(), emp_be.getPassword())) {
        throw new InvalidCredentials("Invalid password");
    }

    
    BankingEntity user = bs.getById(be.getAccountNo())
            .orElseThrow(() -> new UserNotFound("User not found"));

    user.setStatus("INACTIVE");
    bs.openNewAccount(user);

    return "Deleted successfully";
}


@GetMapping("/name/{accountHolder}")
public Optional<BankingEntity> findByUserName(@PathVariable String accountHolder) {
    return bs.findByName(accountHolder);
}

@GetMapping("/checkingbal/{acno}/{pin}")
public double checkingBalance(@PathVariable long acno, @PathVariable int pin) {

    BankingEntity prev = bs.getById(acno)
            .orElseThrow(() -> new UserNotFound("No User Found"));

    checkAccountActive(prev);
    validatePIN(prev.getPinNo(), pin);

    return prev.getBalance();
}

@PutMapping("/status/{empNo}/{acnumber}")
public BankingEntity changeStatus(
        @PathVariable long empNo,
        @PathVariable long acnumber) {

   
    BankingEntity admin = bs.getById(empNo)
            .orElseThrow(() -> new AdminNotFound("Admin not found"));

    
    if (!(admin.getRole().equalsIgnoreCase("CEO")
            || admin.getRole().equalsIgnoreCase("Founder")
            || admin.getRole().equalsIgnoreCase("Manager"))) {

        throw new RoleMisMatch("You are not authorized to change account status");
    }

    BankingEntity user = bs.getById(acnumber)
            .orElseThrow(() -> new UserNotFound("User not found"));

    
    if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
        user.setStatus("INACTIVE");
    } else {
        user.setStatus("ACTIVE");
    }

    return bs.openNewAccount(user);
}


@PutMapping("/transfer/{sender}/{receiver}")
public String transferMoney(
        @PathVariable long sender,
        @PathVariable long receiver,
        @RequestBody TransferRequest send) {

    if(sender == receiver){
        throw new SameAccount("Cannot transfer to same account");
    }

    double amount = send.getAmount();
    int pin = send.getPinNo();   

    BankingEntity from = bs.getById(sender)
            .orElseThrow(() -> new SenderNotFound("Sender not found"));

    BankingEntity to = bs.getById(receiver)
            .orElseThrow(() -> new ReceiverNotFound("Receiver not found"));

    checkAccountActive(from);
    checkAccountActive(to);

    validatePIN(from.getPinNo(), pin);

    if (amount <= 0) {
        throw new InvalidAmount("Invalid amount");
    }
    if (amount> 200000) {
        throw new InvalidAmount("Cannot transfer more than Rs. 2,00,000 at a time");
    }

    if (from.getBalance() < amount) {
        throw new UnavailableBalance("Insufficient balance");
    }

    from.setBalance(from.getBalance() - amount);
    to.setBalance(to.getBalance() + amount);
    cibilService.calculateCibil(from);
    cibilService.calculateCibil(to);

    bs.openNewAccount(from);
    bs.openNewAccount(to);

    txnUtil.saveTxn(from,"TRANSFER-DEBIT", amount,
            "Transferred to A/c " + to.getAccountNo());

    txnUtil.saveTxn(to,"TRANSFER-CREDIT", amount,
            "Received from A/c " + from.getAccountNo());

    emailService.sendMail(
            from.getMail(),
            "DevBank Transfer Alert",
            "Dear " + from.getAccountHolder() + ",\n\n" +
            "₹" + amount + " transferred successfully to A/c " +
            to.getAccountNo() + ".\n\n" +
            "Available Balance: ₹" + from.getBalance() +
            "\n\n- DevBank"
    );

    emailService.sendMail(
            to.getMail(),
            "DevBank Credit Alert",
            "Dear " + to.getAccountHolder() + ",\n\n" +
            "₹" + amount + " received from A/c " +
            from.getAccountNo() + ".\n\n" +
            "Available Balance: ₹" + to.getBalance() +
            "\n\n- DevBank"
    );

    return "Transaction Successful & Mail Sent";
}



@PutMapping("/linkPan/{accountNo}")
public ResponseEntity<String> linkPAN(
        @PathVariable long accountNo,
        @RequestBody BankingEntity req) {

    BankingEntity user = bs.getById(accountNo)
            .orElseThrow(() -> new AccountNotFound("Account not found"));

    checkAccountActive(user);

    String pan = req.getPanNo();

    if (pan == null || pan.isBlank()) {
        throw new PANInvalid("PAN cannot be empty");
    }

    pan = pan.toUpperCase();

    if (!pan.matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
        throw new PANInvalid("Invalid PAN format");
    }
    if (user.getPanNo() != null &&
        pan.equalsIgnoreCase(user.getPanNo())) {

        return ResponseEntity.ok("PAN already linked to this account");
    }

    if (bs.panExists(pan)) {
        throw new PANInvalid("This PAN is already linked with another account");
    }
    if (user.getPanNo() != null && !user.getPanNo().isBlank()) {
        throw new PANInvalid("PAN already linked and cannot be changed");
    }

    
    user.setPanNo(pan);
    bs.updateUser(user);

    emailService.sendMail(
            user.getMail(),
            "PAN Linked - DevBank",
            "Dear " + user.getAccountHolder()
                    + ",\n\nYour PAN has been linked successfully.\n\n- DevBank"
    );

    return ResponseEntity.ok("PAN successfully linked");
}


@PostMapping("/miniStatement")
public String miniStatement(@RequestBody MiniPin req) throws Exception {

    BankingEntity user = bs.getById(req.getAccountNo())
            .orElseThrow(() -> new UserNotFound("User not found"));

    checkAccountActive(user);

    
    validatePIN(user.getPinNo(), req.getPin());

    
    notify.sendMiniStatement(user, "Your mini statement is ready");

    return "Mini statement sent successfully to registered email";
}
@PostMapping("/fetch/sendOtp")
public Map<String, String> sendOtpForRegister(@RequestBody Map<String, String> req) {

    String mail = req.get("mail");

    Optional<BankingEntity> optionalUser = bs.findByMail(mail);

    if(optionalUser.isEmpty()) {
        throw new UserNotFound("User Not Found");
    }

    BankingEntity user = optionalUser.get();

    int otp = otpService.generateOTP(mail);

    emailService.sendMail(
        mail,
        "DevBank OTP",
        "Dear " + user.getAccountHolder() + ",\n\n"
        + "Your OTP is: " + otp + "\n\n"
        + "Do not share OTP.\n\n"
        + "- DevBank"
    );

    Map<String,String> res = new HashMap<>();

    res.put("message","OTP sent successfully");
    res.put("name", user.getAccountHolder());

    return res;
}
@PostMapping("/creditcard")
public String applyCreditCard(@RequestBody CreditCardRequest req) {

    BankingEntity user = bs.getById(req.getAccountNo())
            .orElseThrow(() -> new UserNotFound("User not found"));

    checkAccountActive(user);

    LocalDate dbDob = user.getAge()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

    LocalDate requestDob = req.getDob()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

    if (!dbDob.equals(requestDob)) {
        throw new InvalidDOB("Date of Birth does not match with our records");
    }

 
    int age = Period.between(dbDob, LocalDate.now()).getYears();

    if (age < 21) {
        throw new AgeValidation("Credit card eligibility age is 21 years");
    }
    long totalCards = creditCardService.countCards(user.getAccountNo());

    if (totalCards >= 3) {
        throw new CreditLimitReached("Credit Card limit reached (Maximum 3 allowed)");
    }

    String type = req.getCardType().toUpperCase();

    if (creditCardService.cardTypeExists(user.getAccountNo(), type)) {
        throw new SameTypeCreditCard(
                "You already have a " + type + " credit card");
    }

    CreditCardEntity card = new CreditCardEntity();
    card.setAccountNo(user.getAccountNo());
    card.setCardType(type);
    card.setCardNumber("DEV" + System.currentTimeMillis());
    card.setIssueDate(new Date());
    card.setExpiryDate(
            Date.from(LocalDate.now().plusYears(5)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant())
    );
    card.setCreditLimit(100000);
    card.setStatus("ACTIVE");

    creditCardService.save(card);
    emailService.sendMail(
            user.getMail(),
            "DevBank Credit Card Approved 🎉",
            "Dear " + user.getAccountHolder() + ",\n\n"
                    + "Your " + type + " Credit Card has been approved successfully.\n\n"
                    + "Thank you for choosing DevBank.\n\n"
                    + "- DevBank"
    );

    return "Credit Card issued successfully";
}



//@PostMapping("/login")
//public String login(@RequestBody LoginRequest req) {
//
//    BankingEntity user = bs.findByMail(req.getEmail())
//            .orElseThrow(() -> new UserNotFound("User not found"));
//
//   
//    if (!encoder.matches(req.getPassword(), user.getPassword())) {
//        throw new InvalidCredentials("Invalid password");
//    }
//
//
//    String role = user.getRole();
//    if (!(role.equalsIgnoreCase("CEO")
//            || role.equalsIgnoreCase("Founder")
//            || role.equalsIgnoreCase("Manager"))) {
//
//        throw new RoleMisMatch("Only CEO / Founder / Manager can login");
//    }
//
//    return "Login successful for role: " + role;
//}
////
//@PostMapping("/login")
//public BankingEntity login(@RequestBody LoginRequest req) {
//
//    BankingEntity user = bs.findByMail(req.getEmail())
//            .orElseThrow(() -> new UserNotFound("User not found"));
//
//    if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
//        throw new InactiveAccount("Account not active");
//    }
//
//    if (!encoder.matches(req.getPassword(), user.getPassword())) {
//        throw new InvalidCredentials("Invalid password");
//    }
//
//    return user;  // 👈 return full user
//}
@PostMapping("/login")
public BankingEntity login(@RequestBody LoginRequest req) {
    return bs.login(req);
}


@PutMapping("/editMyProfile/{accountNo}")
public ResponseEntity<BankingEntity> editMyProfile(
        @PathVariable long accountNo,
        @RequestBody UpdateUserDTO req) {

    BankingEntity user = bs.getById(accountNo)
            .orElseThrow(() -> new UserNotFound("User not found"));

    checkAccountActive(user);
    if (req.getMail() != null && !req.getMail().isBlank()) {
        user.setMail(req.getMail());
    }
    if (req.getAccountHolder() != null && !req.getAccountHolder().isBlank()) {
        user.setAccountHolder(req.getAccountHolder());
    }
    if (req.getMobile() != null && !req.getMobile().isBlank()) {
        user.setMobile(req.getMobile());
    }
    if (req.getPassword() != null && !req.getPassword().isBlank()) {
        user.setPassword(encoder.encode(req.getPassword()));
    }
    
//        user.setPinNo(req.getPinNo());
    

    
    if (req.getAge() != null) {
        user.setAge(req.getAge());
    }

    BankingEntity updatedUser = bs.updateUser(user);

    return ResponseEntity.ok(updatedUser);
}

@PostMapping("/forgotPin")
public String forgotPin(@RequestBody ForgotPin req) {

    BankingEntity user = bs.getById(req.getAccountNo())
        .orElseThrow(() -> new UserNotFound("Account not found"));

    if (!user.getMail().equals(req.getMail())) {
        throw new InvalidCredentials("Email mismatch");
    }

    
    if (user.getPinNo() == 0) {
        throw new PinNotSetException("Please set PIN first before using Forgot PIN");
    }

    otpService.validateOTP(req.getMail(), req.getOtp());

    user.setPinNo(req.getNewPin());
    bs.openNewAccount(user);

    return "PIN Updated Successfully";
}

@PostMapping("/forgotPassword")
public String forgotPassword(@RequestBody ForgotPassword req) {

    BankingEntity user = bs.getById(req.getAccountNo())
            .orElseThrow(() -> new UserNotFound("User not found"));

    checkAccountActive(user);

    if (!user.getMail().equalsIgnoreCase(req.getMail())) {
        throw new InvalidCredentials("Email does not match");
    }

    if (!otpService.validateOTP(user.getMail(), req.getOtp())) {
        throw new InvalidOTP("Invalid OTP");
    }

    if (req.getNewPassword() == null || req.getNewPassword().isBlank()) {
        throw new InvalidCredentials("Password cannot be empty");
    }

  
    user.setPassword(encoder.encode(req.getNewPassword()));

    bs.updateUser(user);

    emailService.sendMail(
            user.getMail(),
            "DevBank Password Reset Successful",
            "Dear " + user.getAccountHolder()
                    + ",\n\nYour password has been reset successfully.\n\n- DevBank"
    );

    return "Password reset successful";
}
@GetMapping("/history/{accountNo}")
public ResponseEntity<?> getTransactionHistory(@PathVariable String accountNo){

    try{

        List<PdfEntity> list = txnUtil.getTxnHistory(accountNo);

        return ResponseEntity.ok(list);

    }catch(Exception e){

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error fetching history");

    }

}
@GetMapping("/txn/{id}")
public PdfEntity getTxnById(@PathVariable Long id){
    return txnUtil.getTxnById(id);
}



@GetMapping("/cibilScore/{accountNo}")
public CibilDTO getCibilScore(@PathVariable long accountNo) {

    BankingEntity user = bs.getById(accountNo)
            .orElseThrow(() -> new UserNotFound("User not found"));

    checkAccountActive(user);

    int score = cibilService.calculateCibil(user);

    return new CibilDTO(user.getAccountNo(), score);
}

}







