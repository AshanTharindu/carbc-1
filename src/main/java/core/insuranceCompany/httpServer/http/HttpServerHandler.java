package core.insuranceCompany.httpServer.http;

import com.google.common.cache.Cache;
import com.google.common.io.Resources;
import controller.Controller;
import core.insuranceCompany.InsuranceRecord;
import core.insuranceCompany.dao.InsuranceJDBCDAO;
import core.serviceStation.Service;
import core.serviceStation.ServiceRecord;
import core.serviceStation.ServiceType;
import core.serviceStation.dao.ServiceJDBCDAO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Cache<Long, String> dataCache;

    public HttpServerHandler(Cache<Long, String> dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Connected!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // Check for invalid http data:
        if(msg.decoderResult() != DecoderResult.SUCCESS ) {
            ctx.close();
            System.out.println("closed");
            return;
        }


        if (msg.uri().equals("/insuranceCompany")) {
            serveStatic(ctx, "/index.html");
        } else if (msg.uri().equals("/insuranceCompany/storeInsuranceData")) {
            if (msg.method().equals(HttpMethod.POST)){
                storeInsuranceData(ctx, msg);
            }
        }else if (msg.uri().equals("/insuranceCompany/getVehicleInfo")) {
            if (msg.method().equals(HttpMethod.POST)){
                getVehicleInfo(ctx, msg);
            }
        }else if (msg.uri().equals("/insuranceCompany/setServiceType")) {
            if (msg.method().equals(HttpMethod.POST)){
                setServiceTypes(ctx, msg);
            }
        }else if (msg.uri().equals("/insuranceCompany/registerCustomer")) {
            if (msg.method().equals(HttpMethod.POST)){
//                setServiceTypes(ctx, msg);
            }
        } else {
            serveStatic(ctx, msg.uri());
        }

    }

    private void storeInsuranceData(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException, ParseException {
        //decode request
        ByteBuf data = msg.content();
        int readableBytes = data.readableBytes();
        String body = data.toString(StandardCharsets.UTF_8);

        //convert the text to JSON object from here.
        JSONObject jsonObject = new JSONObject(body);
        System.out.println(jsonObject);

        InsuranceRecord insuranceRecord = new InsuranceRecord();

        insuranceRecord.setVehicle_id(jsonObject.getString("vehicleId"));
        insuranceRecord.setInsured_date(new Timestamp(System.currentTimeMillis()));
        insuranceRecord.setInsurance_id(jsonObject.getInt("insuranceId"));
        insuranceRecord.setInsurance_number(jsonObject.getString("insuranceNumber"));
        Date validFrom = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("validFrom"));
        insuranceRecord.setValid_from(validFrom);
        Date validTo = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("validTo"));
        insuranceRecord.setValid_to(validTo);


        InsuranceJDBCDAO.getInstance().addInsuranceRecord(insuranceRecord);

        //writing response
        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
    }

    private void getVehicleInfo(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException {
        //decode request
        ByteBuf data = msg.content();
        int readableBytes = data.readableBytes();
        String body = data.toString(StandardCharsets.UTF_8);

        //convert the text to JSON object from here.
        JSONObject jsonObject = new JSONObject(body);
        System.out.println(jsonObject);

        String vehicleNumber = jsonObject.getString("vehicleId");

        JSONArray vehicleData = ServiceJDBCDAO.getInstance().getAllServiceRecords(vehicleNumber);
        String stringVehicleDara = vehicleData.toString();

        try {
            byte[] raw = stringVehicleDara.getBytes(StandardCharsets.UTF_8);
            ByteBuf content = Unpooled.wrappedBuffer(raw);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ctx.write(response);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            serve404(ctx);
        }

//        StringBuilder sb = new StringBuilder();
//
//        ByteBuf content = Unpooled.copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
//        ctx.write(response);
    }

    private void setServiceTypes(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException {
        //decode request
        ByteBuf data = msg.content();
        int readableBytes = data.readableBytes();
        String body = data.toString(StandardCharsets.UTF_8);

        //convert the text to JSON object from here.
        JSONObject jsonObject = new JSONObject(body);
        System.out.println(jsonObject);

        String serviceType = jsonObject.getString("serviceType");

        ServiceType serviceTypeObject = new ServiceType(serviceType);
        ServiceJDBCDAO.getInstance().addServiceType(serviceTypeObject);

        //writing response
        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
    }

    private void sendTransactionConfirmation(ChannelHandlerContext ctx, FullHttpRequest msg) throws SQLException {
        //decode request
        ByteBuf data = msg.content();
        int readableBytes = data.readableBytes();
        String body = data.toString(StandardCharsets.UTF_8);

        //convert the text to JSON object from here.
        JSONObject jsonObject = new JSONObject(body);
        System.out.println(jsonObject);

        String blockHash = jsonObject.getString("blockHash");
        Controller controller = new Controller();
        controller.sendConfirmation(blockHash);

        //writing response
        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
    }

    private void requestAdditionalData(ChannelHandlerContext ctx, FullHttpRequest msg){
        //decode request
        ByteBuf data = msg.content();
        int readableBytes = data.readableBytes();
        String body = data.toString(StandardCharsets.UTF_8);

        //convert the text to JSON object from here.
        JSONObject jsonObject = new JSONObject(body);
        System.out.println(jsonObject);

        String blockHash = jsonObject.getString("blockHash");
        String sender = jsonObject.getString("sender");

        Controller controller = new Controller();
        controller.requestAdditionalData(blockHash, sender);

        //writing response
        ByteBuf content = Unpooled.copiedBuffer("successful", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
    }

    private void serveStatic(ChannelHandlerContext ctx, String path) throws Exception {
        try {
            byte[] raw = Resources.toByteArray(Resources.getResource(path.substring(1)));
            ByteBuf content = Unpooled.wrappedBuffer(raw);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, content.readableBytes());
            ctx.write(response);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            serve404(ctx);
        }

    }

    private void serve404(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, 0);
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}


