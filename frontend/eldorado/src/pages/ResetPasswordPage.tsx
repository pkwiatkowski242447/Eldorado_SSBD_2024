import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Button} from "@/components/ui/button.tsx";
import {z} from "zod";
import {useToast} from "@/components/ui/use-toast.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {api} from "@/api/api.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {useNavigate, useParams} from "react-router-dom";

const formSchema = z.object({
    password: z.string().min(2, {message: "Password has to be at least 2 characters long."})
        .max(60, {message: "Password has to be at most 60 characters long."}),
})

function ResetPasswordPage() {
    const {token} = useParams<{ token: string }>();
    const decodedToken = decodeURIComponent(token!);
    const {toast} = useToast();
    const navigate = useNavigate();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            password: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchema>) {
        console.table(values)
        api.resetPassword(decodedToken, values.password)
            .then(() => {
                toast({
                    title: "Password successfully changed!",
                    description: "Press the button to go to the login page.",
                    action: (
                        <div>
                            <Button onClick={() => {
                                navigate('/login', {replace: true});
                            }}>
                                Log in
                            </Button>
                        </div>
                    ),
                });
            })
            .catch((error) => {
                toast({
                    variant: "destructive",
                    description: "Something went wrong. Please try again later.",
                })
                console.log(error.response.data)
            });
    }

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="h-auto w-1/2"/>
            <Card className="mx-auto max-w-2xl">
                <CardHeader>
                    <CardTitle>Reset password</CardTitle>
                    <CardDescription>Enter the new password below</CardDescription>
                </CardHeader>
                <CardContent>
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)}>
                            <div className="flex flex-col space-y-4">
                                <div>
                                    <FormField
                                        control={form.control}
                                        name="password"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel className="text-black">Password</FormLabel>
                                                <FormControl>
                                                    <Input type="password" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <Button type="submit">Change password</Button>
                            </div>
                        </form>
                    </Form>
                </CardContent>
            </Card>
        </div>
    );
}

export default ResetPasswordPage;