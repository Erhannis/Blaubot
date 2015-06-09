package eu.hgross.blaubot.admin;

import java.nio.ByteBuffer;
import java.util.Arrays;

import eu.hgross.blaubot.core.BlaubotConstants;
import eu.hgross.blaubot.messaging.BlaubotMessage;

/**
 * Notifies the king, that a server connection is down on the sending device (uniqueDeviceId included)
 * 
 * @author Henning Gross <mail.to@henning-gross.de>
 *
 */
public class ServerConnectionDownAdminMessage extends AbstractAdminMessage {
    private class MessageDTO {
        String uniqueDeviceId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MessageDTO that = (MessageDTO) o;

            if (uniqueDeviceId != null ? !uniqueDeviceId.equals(that.uniqueDeviceId) : that.uniqueDeviceId != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = uniqueDeviceId != null ? uniqueDeviceId.hashCode() : 0;
            return result;
        }
    }
    private MessageDTO data;

    /**
     *
     * @param mediatorUniqueDeviceId the mediator's unique device id
     */
	public ServerConnectionDownAdminMessage(String mediatorUniqueDeviceId) {
		super(CLASSIFIER_SERVER_CONNECTION_DOWN);
        this.data = new MessageDTO();
        this.data.uniqueDeviceId = mediatorUniqueDeviceId;
	}

	public ServerConnectionDownAdminMessage(BlaubotMessage rawMessage) {
		super(rawMessage);
	}

    @Override
    protected byte[] payloadToBytes() {
        String json = gson.toJson(this.data);
        return json.getBytes(BlaubotConstants.STRING_CHARSET);
    }


    @Override
    protected void setUpFromBytes(ByteBuffer messagePayloadAsBytes) {
        byte[] stringBytes = Arrays.copyOfRange(messagePayloadAsBytes.array(), messagePayloadAsBytes.position(), messagePayloadAsBytes.capacity());
        String json = new String(stringBytes, BlaubotConstants.STRING_CHARSET);
        MessageDTO dto = gson.fromJson(json, MessageDTO.class);
        this.data = dto;
    }

	/**
	 * @return the mediator's unique device id. The mediator is the device holding an actual connection to the server
	 */
	public String getMediatorUniqueDeviceId() {
		return data.uniqueDeviceId;
	}

	@Override
	public String toString() {
		return "ServerConnectionDownAdminMessage [uniqueDeviceId=" + data.uniqueDeviceId + "]";
	}
	
	
	public static void main(String args[]) {
		ServerConnectionDownAdminMessage m = new ServerConnectionDownAdminMessage("blabla");
		ServerConnectionDownAdminMessage around = new ServerConnectionDownAdminMessage(m.toBlaubotMessage());
		System.out.println(""+m.data.uniqueDeviceId.length() + "___" + around.data.uniqueDeviceId.length());
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ServerConnectionDownAdminMessage that = (ServerConnectionDownAdminMessage) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }
}
