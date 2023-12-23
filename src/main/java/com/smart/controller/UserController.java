package com.smart.controller;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

//    method for adding conmmond data for responce
    @ModelAttribute
    public void addCommonData(Model model, Principal principal){
        String userName=principal.getName();
//        System.out.println("user name >"+userName);
        User user=userRepository.getUserByUserName(userName);
//        System.out.println("user  >"+user);
        model.addAttribute("user",user);

    }
//  Dashboard home
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal){
        model.addAttribute("title", "user dashboard");
        return "normal/user_dashboard";
    }

//   open Add form Handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model){
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/process-contact")
    public String proccessContact(@ModelAttribute Contact contact,
                                  @RequestParam("profileImage") MultipartFile file,
                                  Principal principal,
                                  HttpSession session)throws Exception{
        try {
            String name=principal.getName();
            User user=userRepository.getUserByUserName(name);

//            Proccessing and uploading file
            if (file.isEmpty()){
//              if file empty then try over message
                contact.setImage("contact.png");
//                System.out.println("Image is empty");
            }else {
//                file to forlder
                contact.setImage(file.getOriginalFilename());
                File saveFile=new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                System.out.println("Image is uploaded");

            }

            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepository.save(user);
//            System.out.println("Data >>>>"+contact);

            session.setAttribute("message", new Message("Successfully Added !!! Add more","alert-success"));


        }catch (Exception e){
            System.out.println("Error"+e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", new Message("Somthing is wrong", "alert-danger"));
        }

        return "normal/add_contact_form";
    }

//    Show contact handlar

    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal){
        model.addAttribute("title", "Show Contact");
//        sending list of contact
        String userName=principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

//        current page
//        contact per page - 5
        Pageable pageable = PageRequest.of(page, 8);

        Page<Contact> contacts=this.contactRepository.findContactByUser(user.getId(),pageable);
        model.addAttribute("contacts",contacts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contacts.getTotalPages());
        return "normal/show_contacts";
    }


//    showing particular details

    @RequestMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal){
//        System.out.println("CID"+cId);
        Optional<Contact> contactOptional =this.contactRepository.findById(cId);
        Contact contact=contactOptional.get();
        String userName=principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        if (user.getId() == contact.getUser().getId()) {
            model.addAttribute("contact", contact);
            model.addAttribute("title", contact.getName());
        }

        return "normal/contact_detail";
    }

//    Delete contact handler

    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable("cId") Integer cId, Principal principal, HttpSession httpSession){
        Contact contact =this.contactRepository.findById(cId).get();

        String userName=principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        user.getContacts().remove(contact);
        this.userRepository.save(user);



        httpSession.setAttribute("message", new Message("Deleted Successfully....", "alert-success"));

        return "redirect:/user/show-contacts/0";
    }

//    Open Update form handler

    @PostMapping("/update-contact/{cId}")
    private String updateForm(@PathVariable("cId") Integer cId, Model model){
        model.addAttribute("title", "Update Form");
        Contact contact =this.contactRepository.findById(cId).get();
        model.addAttribute("contact", contact);
        return "normal/update_form";
    }


    @PostMapping("/update-contact")
    public String updateContact(@ModelAttribute Contact contact,
                                  @RequestParam("profileImage") MultipartFile file,
                                  Principal principal,
                                  HttpSession session)throws Exception{
        try {
            String name=principal.getName();
            User user=userRepository.getUserByUserName(name);
            Contact oldcontact=this.contactRepository.findById(contact.getcId()).get();

//            Proccessing and uploading file
            if (file.isEmpty()){
//              if file empty then try over message
//                File deleteFile= new ClassPathResource("static/image").getFile();
//                File file1 = new File(deleteFile,oldcontact.getImage());
//                file1.delete();
                contact.setImage(oldcontact.getImage());
//                System.out.println("Image is empty");
            }else {
//                file to forlder
                contact.setImage(file.getOriginalFilename());
                File saveFile=new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                System.out.println("Image is uploaded");

            }

            contact.setUser(user);
//            user.getContacts().add(contact);
//            this.userRepository.save(user);
            this.contactRepository.save(contact);
            session.setAttribute("message", new Message("Update Successfully your contact !!! ","alert-success"));


        }catch (Exception e){
            System.out.println("Error"+e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", new Message("Somthing is wrong", "alert-danger"));
        }

//        return "redirect:/user/show-contacts/0";
        return "redirect:/user/"+contact.getcId()+"/contact";
    }




//    your profile handler

    @GetMapping("/profile")
    public String yourProfile(Model model){
        model.addAttribute("title", "Profile");
        return "normal/profile";
    }

//    open setting handler

    @GetMapping("/settings")
    public String openSettings(){
        return "normal/settings";
    }


//    change password handler

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword")String newPassword, Principal principal, HttpSession httpSession){
//        System.out.println("OLDPASWORD"+oldPassword);
//        System.out.println("NEWPASWORD"+newPassword);

        String useName= principal.getName();
        User currentUser = this.userRepository.getUserByUserName(useName);
//        System.out.println("passwordold"+currentUser.getPassword());

        if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())){
//            change password
            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);
            httpSession.setAttribute("message", new Message("Your Password is successfully change !!","alert-success"));
        }else {
//            error
            httpSession.setAttribute("message", new Message("Please enter correct old password !!","alert-danger"));
            return "redirect:/user/settings";
        }

        return "redirect:/user/index";
    }

}
