package com.pbl.nagadapi.restcontroller;

import com.pbl.nagadapi.repository.ResponseRepository;
import com.pbl.nagadapi.repository.TransactionResponseRepo;
import com.pbl.nagadapi.entity.ResponseAccCheck;
import com.pbl.nagadapi.entity.ResponseTransaction;
import com.pbl.nagadapi.jwt_auth.JwtRequest;
import com.pbl.nagadapi.jwt_auth.JwtResponse;
import com.pbl.nagadapi.jwt_auth.JwtUserDetailsService;
import com.pbl.nagadapi.jwt_auth.JwtUtils;
import com.pbl.nagadapi.response.ResponseTrStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiController {


    @Value("${response.accNum}")
    private short accNumResConf;

    @Value("${response.trNum}")
    private short trNumResConf;

    @Value("${cbs.uri}")
    private String uri;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private ResponseRepository repository;

    @Autowired
    private TransactionResponseRepo transactionRepo;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        //return ResponseEntity.ok(new JwtResponse(token));
        return new JwtResponse(token);


    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/checkAccount")
    @ResponseStatus(HttpStatus.OK)
    public ResponseAccCheck padmaAuth(@RequestBody Map<String, String> payload){


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        Map<String, String> map = new HashMap<>();
        map.put("outSource", "PadmaIB");
        map.put("requestFlag", "63");
        map.put("accountNumber", payload.get("accNumber"));
        JSONObject jsonObject = new JSONObject(map);

        HttpEntity<String> request =
                new HttpEntity<String>(jsonObject.toString(), headers);
        System.out.println(request);

        ResponseEntity<String> res = restTemplate.postForEntity(uri,request,String.class);

        return  sendResponseAccCheck(res);


        //return  res;
    }

    @PostMapping("/transferToAccount")
    @ResponseStatus(HttpStatus.OK)
    public ResponseTransaction padmaTransaction(@RequestBody Map<String, String> payload){

        if (isDuplicateRefId((String)payload.get("refId"))==true )
        {
            ResponseTransaction tmp = transactionRepo.findDistinctByRefId(payload.get("refId"));
            tmp.setResponseCode((short) 220);
            tmp.setResponseMsg(ResponseTransaction.DUPLICATE_REF);

            return tmp;
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("outSource", "PadmaIB");
        map.put("requestFlag", "6608");
        map.put("accountNumber", payload.get("accNumber"));
        map.put("narrative", payload.get("narrative"));
        map.put("debitAmount", payload.get("debitAmount"));
        JSONObject jsonObject = new JSONObject(map);

        HttpEntity<String> request =
                new HttpEntity<String>(jsonObject.toString(), headers);

        ResponseEntity<String> res = restTemplate.postForEntity(uri,request,String.class);
        return sendResponseTransaction(payload.get("refId"),res);

    }

    @PostMapping("/trStatusCheck")
    @ResponseStatus(HttpStatus.OK)
    public ResponseTrStatus padmaTrStatusCheck(@RequestBody Map<String, String> payload){

        String refId = (String)payload.get("refId");
        String ftId;
        if (isDuplicateRefId(refId)==false )
        {
            String resMsg = ResponseTransaction.NOT_FOUND;
            short resCode = (short) 404;
            ResponseTransaction responseTransaction;
            return new ResponseTrStatus(ResponseTrStatus.FT_NOT_FOUND,ResponseTrStatus.REF_NOT_FOUND,"",0);
        }

        ResponseTransaction tmp = transactionRepo.findDistinctByRefId(refId);
        ftId = tmp.getFtId();

        System.out.println(ftId);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("outSource", "PadmaIB");
        map.put("requestFlag", "66");
        map.put("ftidPB", ftId);
        JSONObject jsonObject = new JSONObject(map);

        HttpEntity<String> request =
                new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> res = restTemplate.postForEntity(uri,request,String.class);

        return sendResponseTrStatus(refId,res);
    }

    private ResponseAccCheck sendResponseAccCheck(HttpEntity<String> request){
        String body = request.getBody();
        JSONObject accResBody = new JSONObject(body);

        String res = (String)accResBody.get("outputOFSResponse");
        String resId = (String)accResBody.get("responseId");
        String resStatus = (String)accResBody.get("status");

        String[] resArray = res.split("\\|");

        String accStatus,accHolderName,resMsg;
        short resCode;
        ResponseAccCheck responseAccCheck;

        int numOfResponse = resArray.length;
        System.out.println(numOfResponse);

        if(accNumResConf == numOfResponse) {
            resCode = 200;
            resMsg = "Account details successfully fetched.";
            accHolderName = resArray[0];
            accStatus = resArray[5];

            responseAccCheck = new ResponseAccCheck(resCode,resMsg,accHolderName,accStatus);
            ResponseAccCheck tmp = repository.save(responseAccCheck);
            repository.flush();
            return tmp;
        }
        else {
            resCode = 400;
            resMsg = "Account Number Invalid";
            accHolderName = null;
            accStatus = null;

            responseAccCheck = new ResponseAccCheck(resCode,resMsg,accHolderName,accStatus);
            ResponseAccCheck tmp = repository.save(responseAccCheck);
            repository.flush();
            return tmp;
        }



    }

    private ResponseTransaction sendResponseTransaction(String refId,HttpEntity<String> request){
        String body = request.getBody();
        JSONObject accResBody = new JSONObject(body);

        String res = (String)accResBody.get("outputOFSResponse");

        String resMsg;
        short resCode;
        ResponseTransaction responseTransaction;


        System.out.println(res);
        if(res.substring(0,2).equals("FT")) {
            resCode = 200;
            resMsg = ResponseTransaction.COMPLETE;
        }
        else {
            resCode = 400;
            resMsg = ResponseTransaction.INCOMPLETE;
            res = ResponseTransaction.NOT_FOUND;
        }

        responseTransaction = new ResponseTransaction(refId, res,resCode,resMsg);
        ResponseTransaction tmp = transactionRepo.save(responseTransaction);
        transactionRepo.flush();
        return tmp;
    }

    private ResponseTrStatus sendResponseTrStatus(String refId,HttpEntity<String> request){
        String body = request.getBody();
        JSONObject accResBody = new JSONObject(body);

        String res = (String)accResBody.get("outputOFSResponse");
        ResponseTrStatus responseTrStatus;

        String[] resArray = res.split("\\|");
        int numInResponse = resArray.length;

        for(String str: resArray){
            System.out.println(str);
        }

        String ftId;
        String creditAcc;
        double creditAmount;

        if(trNumResConf == numInResponse) {
            System.out.println(numInResponse);
            ftId = resArray[0];
            creditAcc = resArray[2];
            creditAmount = Double.parseDouble( resArray[4]);
        }
        else {
            ftId = ResponseTrStatus.FT_NOT_FOUND;
            creditAcc = "";
            creditAmount = 0;
        }
        responseTrStatus = new ResponseTrStatus(ftId,refId,creditAcc,creditAmount);
        return responseTrStatus;
    }

    private boolean isDuplicateRefId(String refId){
        if(transactionRepo.findDistinctByRefId(refId)!=null)return true;
        else return false;
    }



}
