package client

import pojo.SynResult

interface ISendClient {
    SynResult send(def data)
}