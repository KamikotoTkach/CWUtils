package tkachgeek.tkachutils.license;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class License {
  public static String hardcodedKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC0uTgsOw7eVrHjLsr6kSTSu/GzSbsW3paH0ujZp0VncbXUEv/Vk7UiszckhPaqT5WjNcxzRqp/HoP+6MS4ZKq3jT38XO//PlcMED14vudZql+mZrUHFxD3RqNH5G+p52C3xzJ5eyR/pnIxHgYDHGMmUZuYdLFt5ySljhFm5xDd7mPF/7r6Ht+mSeOn82PgQoLfeT8XmijVXInF6pFFTM+1F8Jwo2siJc2S3er0jhpYQl3VHvMfrdmMzrCKOaagX046dD48dDKybHkGAa3nOFPGg2dzox9poXYdzeqgxeN9f7wsSJ1e/E0Q87OA5whO3/dL7c7gOBwG1bEjo/FAjR0fAgMBAAECggEAKYtYi+ZppRfl3bVAiPg7sEvvJIli/j9d+Ew2Je6raDu+Eknp0NWbMqZjGzyLnZwc6H9yB9/rSryfmEY3DrJpgjdwU+ajbxgMF6RWFT9BXJ3rTgvXOpGFX2q4F0s+lh6v359ljYKO4d9H6d2egTGR41moPJQ6w2p6xKoTewXMHVAQFPx8C1CXo7uipByWdodcFH0idyZslC8B3AsKb2/04BiYSdsEweMw7+5ZGiQkt+P1x2q9l1O4CyCZkKCOSqeOWYe4nsha4eUMZAeSZ2HkECw0IZLcdR3pUAnNyloFzDYi7zoR+aPVCqE8maYVknVhS/WHsTFuvcugPCCSXmw4eQKBgQDqJYYTlV+OTFV8Neu0chThXS9vDt4xyU/ySO4L6bvPsWHd1DlTXONdvOVQTFW2kHfONKnDHwEmVdX+bKmIAMm3CXR9/P5dwuBSueoulV2+IA/KEKPDgErd7oBn0M5EJJp38ixJgfZO6lXQQnDpnI8akBkrdfLsLSVWTnjDuyVO+QKBgQDFl0FzZmbbFAsDw2/hvjuWno0491x/Wa2EpraOCF+Fa+RPVUDVTl02IsWvhieahWYOZIZmlvwVIu0pwW2B4Zx0mg3COjoeESsRGQne2idmYnOkw+WspJ958pHZXo3iYOicjmBPnsQrXCWTvPnGFq7ESvPeMDJGgYRpapqL5+aa1wKBgQCV8qXkkLp7NKNiQl3KV2zLzpN7+feNxrBuJwpbkrn2DlDwcOKtYkUxWLE6NUiG/AndgphdHtPrqvi6UPhY4rhLgcCalpSHky95ixpP7tPz2DCEZRebMePAMOcGlRAjT5WkzvBRuur0Ktn93y7UTLDQgAu+a3DAk0Df/q0UHBuOcQKBgQDBUAhS1N7Qe72DAxRU8X7vD1PvZeLzHVOsMkbskriRmksWarwxdKexLTfnUAMzJEwCzMrJV/Js42zQq1Nt/MLLOWU4nyr4+6b85VhI1XoyqyOlEZuE2tLDxlR3LBD4aANsF8eWXihim5aQAhUEfnChzwrkWJb3cAOR9yAYvNtapQKBgQCJCh1OU+dqO/VNdRt4p0W8sUp4ksqML4rqIDcaP2T0lB+5YvPrqwBgf/+r1/aiwMNP3O3KT+sgdh4idgzp6X8LGNe1OpXlTd3/St4Frz1oPnHnez/9f7FE7DVSqT1EDBN68c0l3lAgppPAFq6AgnlVwLSuGd3TJT1QgT43n238+w==";
  
  public static boolean check(String key) {
    try {
      return HardwareUtils.checkHwid(decrypt(key, hardcodedKey));
    } catch (Exception e) {
      return false;
    }
  }
  
  public static String decrypt(String message, String keyString) throws Exception {
    byte[] encryptedMessage = Base64.getDecoder().decode(message);
    byte[] decodedKeyBytes = Base64.getDecoder().decode(keyString);
    
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
    
    Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    
    byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
    return new String(decryptedMessage);
  }
  
  /**
   * Проверяет, валидный ли ключ. Если нет - отключает плагин.
   *
   * @return true, если ключ не валидный. Ожидаемое использование<br>
   * <code> onEnable() {<br>
   * ...<br>
   * License.ifNotValid(key, this) return;<br>
   * ...<br>
   * }
   * </code>
   */
  public boolean ifNotValid(String key, JavaPlugin plugin) {
    if (License.check(key)) {
      plugin.getLogger().info("\u001B[32mКлюч лицензии проверен");
      return false;
    } else {
      plugin.getLogger().warning("\u001B[31mНе удалось проверить лицензию. Можете приобрести её тут: https://vk.com/cwcode.");
      plugin.getLogger().warning("\u001B[31mВаш HWID: " + HardwareUtils.getHwidString());
      plugin.getLogger().warning("\u001B[31mПредоставьте его при покупке");
      Bukkit.getPluginManager().disablePlugin(plugin);
      return true;
    }
  }
}
