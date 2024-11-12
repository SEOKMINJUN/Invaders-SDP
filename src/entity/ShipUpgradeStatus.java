package entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ShipUpgradeStatus {

    private double Speed_increase;
    private int SHOOTING_INTERVAL_increase;
    private int BULLET_SPEED_increase;
    private Double coin_increase;
    private int feverTime_score_increase;

    private int Speed_price;
    private int num_Bullet_price;
    private int Attack_Speed_price;
    private int Coin_Bonus_price;

    Properties properties = new Properties();


    public ShipUpgradeStatus() {}

    public void loadStatus(){
        try (InputStream inputStream = ShipUpgradeStatus.class.getClassLoader().getResourceAsStream("StatusConfig.properties")) {
            if (inputStream == null) {
                System.out.println("FileNotFound");
                return;
            }

            properties.load(inputStream);

            Speed_increase = Double.parseDouble(properties.getProperty("Speed.increase"));
            SHOOTING_INTERVAL_increase = Integer.parseInt(properties.getProperty("SHOOTING_INTERVAL.increase"));
            BULLET_SPEED_increase = Integer.parseInt(properties.getProperty("BULLET_SPEED.increase"));
            coin_increase = Double.parseDouble(properties.getProperty("CoinBonus.increase"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPrice(){
        try (InputStream inputStream = ShipUpgradeStatus.class.getClassLoader().getResourceAsStream("StatusConfig.properties")) {
            if (inputStream == null) {
                System.out.println("FileNotFound");
                return;
            }

            properties.load(inputStream);

            Speed_price = Integer.parseInt(properties.getProperty("Speed.price"));
            num_Bullet_price = Integer.parseInt(properties.getProperty("bullet_number.price"));
            Attack_Speed_price = Integer.parseInt(properties.getProperty("SHOOTING_INTERVAL.price"));
            Coin_Bonus_price = Integer.parseInt(properties.getProperty("CoinBonus.price"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final double getSpeedIn(){
        return Speed_increase;
    }
    public final int getSuootingInIn(){
        return SHOOTING_INTERVAL_increase;
    }
    public final int getBulletSpeedIn(){
        return BULLET_SPEED_increase;
    }
    public final double getCoinIn(){
        return coin_increase;
    }

    public final int getSpeed_price(){
        return Speed_price;
    }
    public final int getCoinBonus_price(){
        return Coin_Bonus_price;
    }
    public final int getAttack_price(){
        return Attack_Speed_price;
    }
    public final int getBullet_price(){
        return num_Bullet_price;
    }
}
