package web_patterns.business;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
public class User {
    @EqualsAndHashCode.Include
    private String username;
    @ToString.Exclude
    private String password;
    private String firstName;
    private String lastName;
    private boolean isAdmin;
    private String email;
}
