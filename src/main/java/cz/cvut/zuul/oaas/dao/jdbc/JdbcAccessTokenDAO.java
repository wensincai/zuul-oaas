package cz.cvut.zuul.oaas.dao.jdbc;

import cz.cvut.zuul.oaas.dao.AccessTokenDAO;
import cz.cvut.zuul.oaas.models.PersistableAccessToken;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static cz.cvut.zuul.oaas.models.PersistableAccessToken.fields.*;
import static cz.cvut.zuul.oaas.utils.JdbcUtils.*;


/**
 *
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class JdbcAccessTokenDAO extends AbstractJdbcGenericDAO<PersistableAccessToken, String> implements AccessTokenDAO {

    //columns extracted from authentication used for searching
    private static final String
            AUTH_KEY = "authentication_key",
            CLIENT_ID = "client_id",
            USER_NAME = "user_name";


    public PersistableAccessToken findOneByAuthentication(OAuth2Authentication authentication) {
        String key = PersistableAccessToken.extractAuthenticationKey(authentication);
        List<PersistableAccessToken> result = findBy(AUTH_KEY, key);

        if (result.isEmpty()) {
            return null;
        }
        PersistableAccessToken accessToken = result.get(0);

        if (!authentication.equals(accessToken.getAuthentication())) {
            delete(accessToken);
            // keep the store consistent (maybe the same user is represented by this auth. but the details have changed)
            save(new PersistableAccessToken(accessToken, authentication));
        }
        return accessToken;
    }

    @SuppressWarnings("unchecked")
    public Collection<OAuth2AccessToken> findByClientId(String clientId) {
        return (Collection) findBy(CLIENT_ID, clientId);
    }

    @SuppressWarnings("unchecked")
    public Collection<OAuth2AccessToken> findByUserName(String userName) {
        return (Collection) findBy(USER_NAME, userName);
    }

    public void deleteByRefreshToken(OAuth2RefreshToken refreshToken) {
        deleteBy(REFRESH_TOKEN, refreshToken.getValue());
    }


    //////// Mapping ////////

    protected String getTableName() {
        return "access_tokens";
    }

    protected RowMapper<PersistableAccessToken> getRowMapper() {
        return new RowMapper<PersistableAccessToken>() {
            public PersistableAccessToken mapRow(final ResultSet rs, int rowNum) throws SQLException {

                PersistableAccessToken token = new PersistableAccessToken(rs.getString(ID));
                OAuth2Authentication authentication = deserialize(rs.getBytes(AUTHENTICATION), OAuth2Authentication.class);
                String refreshToken = rs.getString(REFRESH_TOKEN);

                token.setExpiration( rs.getTimestamp(EXPIRATION) );
                token.setTokenType( rs.getString(TOKEN_TYPE) );
                token.setRefreshToken( refreshToken != null ? new DefaultOAuth2RefreshToken(refreshToken) : null );
                token.setScope( toSet(rs.getArray(SCOPE)) );
                token.setAuthentication( authentication );

                return token;
            }
        };
    }

    protected Object[][] getEntityMapping(PersistableAccessToken e) {
        return new Object[][] {
                { ID,               e.getValue()                     },
                { EXPIRATION,       e.getExpiration()                },
                { TOKEN_TYPE,       e.getTokenType()                 },
                { REFRESH_TOKEN,    e.getRefreshTokenValue()         },
                { SCOPE,            toArray(e.getScope())            },
                { AUTHENTICATION,   serialize(e.getAuthentication()) },
                { AUTH_KEY,         e.getAuthenticationKey()         },
                { CLIENT_ID,        e.getAuthenticatedClientId()     },
                { USER_NAME,        e.getAuthenticatedUsername()     }
        };
    }
}