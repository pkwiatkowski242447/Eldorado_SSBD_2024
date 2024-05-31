import {api} from "@/api/api.ts";
import {Card, CardContent, CardTitle} from "@/components/ui/card.tsx";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";

interface Attribute {
    attributeName: string;
    attributeValue: string;
}

function AttributesPage() {
    const { t } = useTranslation();
    const [attributes, setAttributes] = useState<Attribute[]>([]);


    useEffect(() => {
        const fetchAttributes = async () => {
            const response = await api.getMyAttributes();
            setAttributes(response.data);
        };

        fetchAttributes();
    }, []);

    return (
        <div>
            <Card className="mx-10 w-auto text-left">
                <CardTitle className={"flex justify-center mt-5"}>
                    {t('attributes.title')}
                </CardTitle>
                <CardContent className={"mt-5"}>
                    {attributes.map((attr, index) => (
                        <div key={index}>
                            <strong>{t(attr.attributeName)}:</strong> {attr.attributeValue}
                        </div>
                    ))}
                </CardContent>
            </Card>
        </div>
    );
}

export default AttributesPage;