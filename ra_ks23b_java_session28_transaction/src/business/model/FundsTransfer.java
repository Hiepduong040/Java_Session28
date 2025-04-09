package business.model;

import java.util.Date;

public class FundsTransfer {
    private int id;
    private int senderId;
    private int receiverId;
    private double amount;
    private Date created;
    private boolean status;

    public FundsTransfer(int id, int senderId, int receiverId, double amount, Date created, boolean status) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.created = created;
        this.status = status;
    }

    public int getId() { return id; }
    public int getSenderId() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public double getAmount() { return amount; }
    public Date getCreated() { return created; }
    public boolean isStatus() { return status; }

    @Override
    public String toString() {
        return "FundsTransfer{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", amount=" + amount +
                ", created=" + created +
                ", status=" + status +
                '}';
    }
}