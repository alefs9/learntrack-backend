package com.upc.learntrack.iam.service.impl;

import com.upc.learntrack.course.model.Student;
import com.upc.learntrack.course.model.Teacher;
import com.upc.learntrack.course.repository.StudentRepository;
import com.upc.learntrack.course.repository.TeacherRepository;
import com.upc.learntrack.iam.dto.PendingUserDto;
import com.upc.learntrack.iam.exception.UserNotFoundException;
import com.upc.learntrack.iam.model.User;
import com.upc.learntrack.iam.model.UserStatus;
import com.upc.learntrack.iam.repository.UserRepository;
import com.upc.learntrack.iam.service.AdminService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional(readOnly = true)
    public List<PendingUserDto> getPendingUsers() {
        List<User> users = userRepository.findAllByStatus(UserStatus.PENDING);
        List<PendingUserDto> dtos = new ArrayList<>();

        for (User user : users) {
            String firstName = null;
            String lastName = null;
            String roleName = user.getRole().getName();

            if ("DOCENTE".equals(roleName)) {
                Teacher teacher = teacherRepository.findByUserEmail(user.getEmail()).orElse(null);
                if (teacher != null) {
                    firstName = teacher.getFirstName();
                    lastName = teacher.getLastName();
                }
            } else if ("ESTUDIANTE".equals(roleName)) {
                Student student = studentRepository.findByUserEmail(user.getEmail()).orElse(null);
                if (student != null) {
                    firstName = student.getFirstName();
                    lastName = student.getLastName();
                }
            }

            dtos.add(new PendingUserDto(
                    user.getId(),
                    user.getEmail(),
                    firstName,
                    lastName,
                    roleName,
                    user.getCreatedAt()
            ));
        }
        return dtos;
    }

    @Override
    @Transactional
    public void approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("El usuario no está pendiente de aprobación");
        }

        // Generar código de 6 dígitos
        String code = String.format("%06d", new Random().nextInt(999999));
        user.setVerificationCode(code);
        user.setVerificationAttempts(0);
        // El status sigue siendo PENDING hasta que verifique el código
        userRepository.save(user);

        // Enviar correo con el código
        sendVerificationEmail(user.getEmail(), code);
    }

    @Override
    @Transactional
    public void rejectUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        user.setStatus(UserStatus.REJECTED);
        userRepository.save(user);
        sendRejectionEmail(user.getEmail());
    }

    @Override
    @Transactional
    public void verifyCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("El usuario no está pendiente de verificación");
        }

        if (user.getVerificationAttempts() >= 3) {
            throw new IllegalStateException("Número máximo de intentos excedido. Vuelve a registrarte.");
        }

        if (!code.equals(user.getVerificationCode())) {
            user.setVerificationAttempts(user.getVerificationAttempts() + 1);
            userRepository.save(user);
            throw new IllegalArgumentException("Código incorrecto. Intentos restantes: " + (3 - user.getVerificationAttempts()));
        }

        // Código correcto → activar cuenta
        user.setStatus(UserStatus.ACTIVE);
        user.setVerificationCode(null);
        user.setVerificationAttempts(null);
        userRepository.save(user);
        // Opcional: enviar correo de bienvenida
    }

    // --- Métodos privados para correos ---

    private void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Verifica tu cuenta en LearnTrack");

            Context context = new Context();
            context.setVariable("code", code);
            context.setVariable("email", to);
            String html = templateEngine.process("verification-email", context);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo de verificación", e);
        }
    }

    private void sendRejectionEmail(String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Solicitud de registro rechazada");

            Context context = new Context();
            String html = templateEngine.process("rejection-email", context);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo de rechazo", e);
        }
    }
}