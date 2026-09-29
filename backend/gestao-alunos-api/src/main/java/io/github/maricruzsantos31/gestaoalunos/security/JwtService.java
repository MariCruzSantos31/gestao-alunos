package io.github.maricruzsantos31.gestaoalunos.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey chave;
    private final long expiracao;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiracao) {

        byte[] chaveBytes = Decoders.BASE64.decode(secret);

        this.chave = Keys.hmacShaKeyFor(chaveBytes);
        this.expiracao = expiracao;
    }

    public String gerarToken(String username) {

        Date agora = new Date();
        Date dataExpiracao = new Date(agora.getTime() + expiracao);

        return Jwts.builder()
                .subject(username)
                .issuedAt(agora)
                .expiration(dataExpiracao)
                .signWith(chave)
                .compact();
    }

    public String extrairUsername(String token) {
        return extrairClaims(token).getSubject();
    }

    public boolean tokenValido(String token, UserDetails userDetails) {

        try {
            Claims claims = extrairClaims(token);

            String username = claims.getSubject();
            Date dataExpiracao = claims.getExpiration();

            return username.equals(userDetails.getUsername())
                    && dataExpiracao.after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extrairClaims(String token) {

        return Jwts.parser()
                .verifyWith(chave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}