package cz.itnetwork.service.User;

import cz.itnetwork.dto.user.UserAuthDTO;
import cz.itnetwork.entity.User;

/**
 * Rozhraní aplikační služby zodpovědné za správu uživatelů systému.
 *
 * Definuje kontrakt pro business logiku související s registrací,
 * autentizací a vyhledáváním uživatelů.
 *
 * Implementace této služby zajišťuje komunikaci mezi controllerem
 * a perzistentní vrstvou (repository).
 */
public interface UserService {

    /**
     * Lokální registrace uživatele pomocí emailu a hesla.
     *
     * @param dto datový přenosový objekt obsahující registrační údaje uživatele
     * @return registrovaný uživatel
     */
    User register(UserAuthDTO dto);

    /**
     * Registrace nebo přihlášení uživatele pomocí Google OAuth.
     *
     * Metoda zajišťuje buď přihlášení existujícího uživatele,
     * nebo vytvoření nového účtu na základě Google identity.
     *
     * @param googleId unikátní identifikátor uživatele poskytnutý Googlem
     * @param email    emailová adresa uživatele
     * @param name     celé jméno uživatele z Google profilu
     * @param picture URL profilového obrázku uživatele
     * @return registrovaný nebo přihlášený uživatel
     */
    User registerOrLoginGoogle(String googleId, String email, String name, String picture);

    /**
     * Vyhledání uživatele podle emailové adresy.
     *
     * @param email emailová adresa uživatele
     * @return uživatel nebo null, pokud neexistuje
     */
    User findByEmail(String email);
}
