package com.github.hellorpc.nio.proto;

/**
 * @author George (GeorgeWorld@qq.com)
 */
public final class PacketProto {
    private PacketProto() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
    }

    public interface PacketOrBuilder extends
            // @@protoc_insertion_point(interface_extends:Packet)
            com.google.protobuf.MessageOrBuilder {

        /**
         * <code>required .Packet.PacketType packetType = 1;</code>
         *
         * <pre>
         * 包类型
         * </pre>
         */
        boolean hasPacketType();

        /**
         * <code>required .Packet.PacketType packetType = 1;</code>
         *
         * <pre>
         * 包类型
         * </pre>
         */
        PacketProto.Packet.PacketType getPacketType();

        /**
         * <code>optional string data = 2;</code>
         *
         * <pre>
         * 数据部分（可选，心跳包不包含数据部分）
         * </pre>
         */
        boolean hasData();

        /**
         * <code>optional string data = 2;</code>
         *
         * <pre>
         * 数据部分（可选，心跳包不包含数据部分）
         * </pre>
         */
        java.lang.String getData();

        /**
         * <code>optional string data = 2;</code>
         *
         * <pre>
         * 数据部分（可选，心跳包不包含数据部分）
         * </pre>
         */
        com.google.protobuf.ByteString
        getDataBytes();
    }

    /**
     * Protobuf type {@code Packet}
     */
    public static final class Packet extends
            com.google.protobuf.GeneratedMessage implements
            // @@protoc_insertion_point(message_implements:Packet)
            PacketOrBuilder {
        // Use Packet.newBuilder() to construct.
        private Packet(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private Packet(boolean noInit) {
            this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
        }

        private static final Packet defaultInstance;

        public static Packet getDefaultInstance() {
            return defaultInstance;
        }

        public Packet getDefaultInstanceForType() {
            return defaultInstance;
        }

        private final com.google.protobuf.UnknownFieldSet unknownFields;

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
            return this.unknownFields;
        }

        private Packet(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            initFields();
            int mutable_bitField0_ = 0;
            com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                    com.google.protobuf.UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields,
                                    extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 8: {
                            int rawValue = input.readEnum();
                            PacketProto.Packet.PacketType value = PacketProto.Packet.PacketType.valueOf(rawValue);
                            if (value == null) {
                                unknownFields.mergeVarintField(1, rawValue);
                            } else {
                                bitField0_ |= 0x00000001;
                                packetType_ = value;
                            }
                            break;
                        }
                        case 18: {
                            com.google.protobuf.ByteString bs = input.readBytes();
                            bitField0_ |= 0x00000002;
                            data_ = bs;
                            break;
                        }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(
                        e.getMessage()).setUnfinishedMessage(this);
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return PacketProto.internal_static_Packet_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return PacketProto.internal_static_Packet_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            PacketProto.Packet.class, PacketProto.Packet.Builder.class);
        }

        public static com.google.protobuf.Parser<Packet> PARSER =
                new com.google.protobuf.AbstractParser<Packet>() {
                    public Packet parsePartialFrom(
                            com.google.protobuf.CodedInputStream input,
                            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                            throws com.google.protobuf.InvalidProtocolBufferException {
                        return new Packet(input, extensionRegistry);
                    }
                };

        @java.lang.Override
        public com.google.protobuf.Parser<Packet> getParserForType() {
            return PARSER;
        }

        /**
         * Protobuf enum {@code Packet.PacketType}
         *
         * <pre>
         * 包的类型
         * </pre>
         */
        public enum PacketType
                implements com.google.protobuf.ProtocolMessageEnum {
            /**
             * <code>HEARTBEAT = 1;</code>
             *
             * <pre>
             * 心跳包
             * </pre>
             */
            HEARTBEAT(0, 1),
            /**
             * <code>DATA = 2;</code>
             *
             * <pre>
             * 非心跳包
             * </pre>
             */
            DATA(1, 2),;

            /**
             * <code>HEARTBEAT = 1;</code>
             *
             * <pre>
             * 心跳包
             * </pre>
             */
            public static final int HEARTBEAT_VALUE = 1;
            /**
             * <code>DATA = 2;</code>
             *
             * <pre>
             * 非心跳包
             * </pre>
             */
            public static final int DATA_VALUE = 2;


            public final int getNumber() {
                return value;
            }

            public static PacketType valueOf(int value) {
                switch (value) {
                    case 1:
                        return HEARTBEAT;
                    case 2:
                        return DATA;
                    default:
                        return null;
                }
            }

            public static com.google.protobuf.Internal.EnumLiteMap<PacketType>
            internalGetValueMap() {
                return internalValueMap;
            }

            private static com.google.protobuf.Internal.EnumLiteMap<PacketType>
                    internalValueMap =
                    new com.google.protobuf.Internal.EnumLiteMap<PacketType>() {
                        public PacketType findValueByNumber(int number) {
                            return PacketType.valueOf(number);
                        }
                    };

            public final com.google.protobuf.Descriptors.EnumValueDescriptor
            getValueDescriptor() {
                return getDescriptor().getValues().get(index);
            }

            public final com.google.protobuf.Descriptors.EnumDescriptor
            getDescriptorForType() {
                return getDescriptor();
            }

            public static final com.google.protobuf.Descriptors.EnumDescriptor
            getDescriptor() {
                return PacketProto.Packet.getDescriptor().getEnumTypes().get(0);
            }

            private static final PacketType[] VALUES = values();

            public static PacketType valueOf(
                    com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new java.lang.IllegalArgumentException(
                            "EnumValueDescriptor is not for this type.");
                }
                return VALUES[desc.getIndex()];
            }

            private final int index;
            private final int value;

            private PacketType(int index, int value) {
                this.index = index;
                this.value = value;
            }

            // @@protoc_insertion_point(enum_scope:Packet.PacketType)
        }

        private int bitField0_;
        public static final int PACKETTYPE_FIELD_NUMBER = 1;
        private PacketProto.Packet.PacketType packetType_;

        /**
         * <code>required .Packet.PacketType packetType = 1;</code>
         *
         * <pre>
         * 包类型
         * </pre>
         */
        public boolean hasPacketType() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        /**
         * <code>required .Packet.PacketType packetType = 1;</code>
         *
         * <pre>
         * 包类型
         * </pre>
         */
        public PacketProto.Packet.PacketType getPacketType() {
            return packetType_;
        }

        public static final int DATA_FIELD_NUMBER = 2;
        private java.lang.Object data_;

        /**
         * <code>optional string data = 2;</code>
         *
         * <pre>
         * 数据部分（可选，心跳包不包含数据部分）
         * </pre>
         */
        public boolean hasData() {
            return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        /**
         * <code>optional string data = 2;</code>
         *
         * <pre>
         * 数据部分（可选，心跳包不包含数据部分）
         * </pre>
         */
        public java.lang.String getData() {
            java.lang.Object ref = data_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs =
                        (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    data_ = s;
                }
                return s;
            }
        }

        /**
         * <code>optional string data = 2;</code>
         *
         * <pre>
         * 数据部分（可选，心跳包不包含数据部分）
         * </pre>
         */
        public com.google.protobuf.ByteString
        getDataBytes() {
            java.lang.Object ref = data_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b =
                        com.google.protobuf.ByteString.copyFromUtf8(
                                (java.lang.String) ref);
                data_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        private void initFields() {
            packetType_ = PacketProto.Packet.PacketType.HEARTBEAT;
            data_ = "";
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized == 1) return true;
            if (isInitialized == 0) return false;

            if (!hasPacketType()) {
                memoizedIsInitialized = 0;
                return false;
            }
            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output)
                throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeEnum(1, packetType_.getNumber());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                output.writeBytes(2, getDataBytes());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeEnumSize(1, packetType_.getNumber());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                size += com.google.protobuf.CodedOutputStream
                        .computeBytesSize(2, getDataBytes());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        private static final long serialVersionUID = 0L;

        @java.lang.Override
        protected java.lang.Object writeReplace()
                throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static PacketProto.Packet parseFrom(
                com.google.protobuf.ByteString data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static PacketProto.Packet parseFrom(
                com.google.protobuf.ByteString data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static PacketProto.Packet parseFrom(byte[] data)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static PacketProto.Packet parseFrom(
                byte[] data,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static PacketProto.Packet parseFrom(java.io.InputStream input)
                throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static PacketProto.Packet parseFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static PacketProto.Packet parseDelimitedFrom(java.io.InputStream input)
                throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static PacketProto.Packet parseDelimitedFrom(
                java.io.InputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static PacketProto.Packet parseFrom(
                com.google.protobuf.CodedInputStream input)
                throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static PacketProto.Packet parseFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(PacketProto.Packet prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
                com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /**
         * Protobuf type {@code Packet}
         */
        public static final class Builder extends
                com.google.protobuf.GeneratedMessage.Builder<Builder> implements
                // @@protoc_insertion_point(builder_implements:Packet)
                PacketProto.PacketOrBuilder {
            public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
                return PacketProto.internal_static_Packet_descriptor;
            }

            protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
                return PacketProto.internal_static_Packet_fieldAccessorTable
                        .ensureFieldAccessorsInitialized(
                                PacketProto.Packet.class, PacketProto.Packet.Builder.class);
            }

            // Construct using common.PacketProto.Packet.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(
                    com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                packetType_ = PacketProto.Packet.PacketType.HEARTBEAT;
                bitField0_ = (bitField0_ & ~0x00000001);
                data_ = "";
                bitField0_ = (bitField0_ & ~0x00000002);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
                return PacketProto.internal_static_Packet_descriptor;
            }

            public PacketProto.Packet getDefaultInstanceForType() {
                return PacketProto.Packet.getDefaultInstance();
            }

            public PacketProto.Packet build() {
                PacketProto.Packet result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            public PacketProto.Packet buildPartial() {
                PacketProto.Packet result = new PacketProto.Packet(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.packetType_ = packetType_;
                if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
                    to_bitField0_ |= 0x00000002;
                }
                result.data_ = data_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof PacketProto.Packet) {
                    return mergeFrom((PacketProto.Packet) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(PacketProto.Packet other) {
                if (other == PacketProto.Packet.getDefaultInstance()) return this;
                if (other.hasPacketType()) {
                    setPacketType(other.getPacketType());
                }
                if (other.hasData()) {
                    bitField0_ |= 0x00000002;
                    data_ = other.data_;
                    onChanged();
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                if (!hasPacketType()) {

                    return false;
                }
                return true;
            }

            public Builder mergeFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                    throws java.io.IOException {
                PacketProto.Packet parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (PacketProto.Packet) e.getUnfinishedMessage();
                    throw e;
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            private int bitField0_;

            private PacketProto.Packet.PacketType packetType_ = PacketProto.Packet.PacketType.HEARTBEAT;

            /**
             * <code>required .Packet.PacketType packetType = 1;</code>
             *
             * <pre>
             * 包类型
             * </pre>
             */
            public boolean hasPacketType() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            /**
             * <code>required .Packet.PacketType packetType = 1;</code>
             *
             * <pre>
             * 包类型
             * </pre>
             */
            public PacketProto.Packet.PacketType getPacketType() {
                return packetType_;
            }

            /**
             * <code>required .Packet.PacketType packetType = 1;</code>
             *
             * <pre>
             * 包类型
             * </pre>
             */
            public Builder setPacketType(PacketProto.Packet.PacketType value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                packetType_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required .Packet.PacketType packetType = 1;</code>
             *
             * <pre>
             * 包类型
             * </pre>
             */
            public Builder clearPacketType() {
                bitField0_ = (bitField0_ & ~0x00000001);
                packetType_ = PacketProto.Packet.PacketType.HEARTBEAT;
                onChanged();
                return this;
            }

            private java.lang.Object data_ = "";

            /**
             * <code>optional string data = 2;</code>
             *
             * <pre>
             * 数据部分（可选，心跳包不包含数据部分）
             * </pre>
             */
            public boolean hasData() {
                return ((bitField0_ & 0x00000002) == 0x00000002);
            }

            /**
             * <code>optional string data = 2;</code>
             *
             * <pre>
             * 数据部分（可选，心跳包不包含数据部分）
             * </pre>
             */
            public java.lang.String getData() {
                java.lang.Object ref = data_;
                if (!(ref instanceof java.lang.String)) {
                    com.google.protobuf.ByteString bs =
                            (com.google.protobuf.ByteString) ref;
                    java.lang.String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        data_ = s;
                    }
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>optional string data = 2;</code>
             *
             * <pre>
             * 数据部分（可选，心跳包不包含数据部分）
             * </pre>
             */
            public com.google.protobuf.ByteString
            getDataBytes() {
                java.lang.Object ref = data_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b =
                            com.google.protobuf.ByteString.copyFromUtf8(
                                    (java.lang.String) ref);
                    data_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>optional string data = 2;</code>
             *
             * <pre>
             * 数据部分（可选，心跳包不包含数据部分）
             * </pre>
             */
            public Builder setData(
                    java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                data_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>optional string data = 2;</code>
             *
             * <pre>
             * 数据部分（可选，心跳包不包含数据部分）
             * </pre>
             */
            public Builder clearData() {
                bitField0_ = (bitField0_ & ~0x00000002);
                data_ = getDefaultInstance().getData();
                onChanged();
                return this;
            }

            /**
             * <code>optional string data = 2;</code>
             *
             * <pre>
             * 数据部分（可选，心跳包不包含数据部分）
             * </pre>
             */
            public Builder setDataBytes(
                    com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                data_ = value;
                onChanged();
                return this;
            }

            // @@protoc_insertion_point(builder_scope:Packet)
        }

        static {
            defaultInstance = new Packet(true);
            defaultInstance.initFields();
        }

        // @@protoc_insertion_point(class_scope:Packet)
    }

    private static final com.google.protobuf.Descriptors.Descriptor
            internal_static_Packet_descriptor;
    private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internal_static_Packet_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\n\014Packet.proto\"e\n\006Packet\022&\n\npacketType\030\001" +
                        " \002(\0162\022.Packet.PacketType\022\014\n\004data\030\002 \001(\t\"%" +
                        "\n\nPacketType\022\r\n\tHEARTBEAT\020\001\022\010\n\004DATA\020\002B\rB" +
                        "\013PacketProto"
        };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
                new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
                    public com.google.protobuf.ExtensionRegistry assignDescriptors(
                            com.google.protobuf.Descriptors.FileDescriptor root) {
                        descriptor = root;
                        return null;
                    }
                };
        com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                        }, assigner);
        internal_static_Packet_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_Packet_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                internal_static_Packet_descriptor,
                new java.lang.String[]{"PacketType", "Data",});
    }

    // @@protoc_insertion_point(outer_class_scope)
}
